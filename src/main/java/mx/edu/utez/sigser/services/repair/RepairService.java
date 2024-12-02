package mx.edu.utez.sigser.services.repair;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import mx.edu.utez.sigser.controllers.email.dto.EmailDto;
import mx.edu.utez.sigser.controllers.repair.dtos.DiagnosisRepairDTO;
import mx.edu.utez.sigser.controllers.repair.dtos.QuotationRepairDTO;
import mx.edu.utez.sigser.controllers.repair.dtos.RepairRepairDTO;
import mx.edu.utez.sigser.models.device.DeviceRepository;
import mx.edu.utez.sigser.models.diagnostic_image.DiagnosticImage;
import mx.edu.utez.sigser.models.repair.Repair;
import mx.edu.utez.sigser.models.repair.RepairRepository;
import mx.edu.utez.sigser.models.repair_image.RepairImage;
import mx.edu.utez.sigser.models.repair_status.RepairStatusRepository;
import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.models.user.UserRepository;
import mx.edu.utez.sigser.utils.EmailService;
import mx.edu.utez.sigser.utils.ImageService;
import mx.edu.utez.sigser.utils.MqttService;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RepairService {

    private final RepairRepository repairRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RepairStatusRepository repairStatusRepository;
    private final EmailService emailService;
    private final ImageService imageService;
    private final MqttService mqttService;



    public RepairService(RepairRepository repairRepository, UserRepository userRepository, DeviceRepository deviceRepository, RepairStatusRepository repairStatusRepository, EmailService emailService, ImageService imageService, MqttService mqttService) {
        this.repairRepository = repairRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.repairStatusRepository = repairStatusRepository;
        this.emailService = emailService;
        this.imageService = imageService;
        this.mqttService = mqttService;
    }


    @Transactional(readOnly = true)
    public Response<List<Repair>> getAll() {
        return new Response<>(
                this.repairRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<Repair> getById(Long id) {
        if(this.repairRepository.existsById(id)) {
            return new Response<>(
                    this.repairRepository.findById(id).orElse(null),
                    false,
                    200,
                    "OK"
            );
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(readOnly = true)
    public Response<List<Repair>> getAllByClientId(Long clientId) {
        User client = this.userRepository.findById(clientId).orElse(null);
        if(client != null) {
            if(client.getUserType().getId() == 4) {
                return new Response<>(
                        this.repairRepository.findAllByClientId(clientId),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "User is not a client"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "User not found"
            );
        }
    }


    @Transactional(readOnly = true)
    public Response<List<Repair>> getAllByTechnicianId(Long technicianId) {
        User technician = this.userRepository.findById(technicianId).orElse(null);
        if(technician != null) {
            if(technician.getUserType().getId() == 3) {
                return new Response<>(
                        this.repairRepository.findAllByTechnicianId(technicianId),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "User is not a technician"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "User not found"
            );
        }
    }

    //TODO: Implement MQTT notification for every user in the repair process using topic like /sigser/notifications/admin/repair/{repairId}

    @Transactional(rollbackFor = Exception.class)
    public Response<byte[]> receivedStatus (Repair repair) throws WriterException {
        if(repairRepository.findAllByDeviceIdAndDifferentRepairStatusCollected(repair.getDevice().getId()).isEmpty()) {
            if (repair.getClient().getId() != null && repair.getTechnician().getId() != null) {
                User client = this.userRepository.findById(repair.getClient().getId()).orElse(null);
                User technician = this.userRepository.findById(repair.getTechnician().getId()).orElse(null);
                assert client != null;
                assert technician != null;
                if(client.getUserType().getId() == 4 && technician.getUserType().getId() == 3) {
                    repair.setClient(client);
                    repair.setTechnician(technician);
                } else {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "Client or Technician not found"
                    );
                }
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "Client or Technician not found"
                );
            }

            repair.setRepairStatus(this.repairStatusRepository.findById(1L).orElse(null));
            repair.setEntry_date(LocalDateTime.now());
            repair.setDevice(this.deviceRepository.findById(repair.getDevice().getId()).orElse(null));



            Repair newRepair = this.repairRepository.saveAndFlush(repair);

            this.emailService.sendMail(new EmailDto(newRepair.getClient().getEmail(), newRepair.getClient().getName(), "", newRepair.getDevice().toStringEmail() , null, null,0), "changeStatus-received");
            this.mqttService.sendNotificationAdmin("Nuevo dispositivo ingresado al taller. Número de reparación: " + newRepair.getId());
            this.mqttService.sendNotificationTechnician("Nuevo dispositivo ingresado al taller y asignado a ti. Número de reparación: " + newRepair.getId(),  "/" + newRepair.getTechnician().getId());
            this.mqttService.sendNotificationClient("Tu dispositivo ha sido ingresado al taller. Dispositivo : " + newRepair.getDevice().getBrand() + " - " + newRepair.getDevice().getModel() , "/" + newRepair.getClient().getId());

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(newRepair.toString(), BarcodeFormat.QR_CODE, 300, 300);
            byte[] qrCode = null;

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                qrCode = outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new Response<>(
                    qrCode,
                    false,
                    201,
                    "Created"
            );

        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "There is already a repair in progress for this device"
            );
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> startDiagnosisStatus (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() == 1) {
                repair.setRepairStatus(this.repairStatusRepository.findById(2L).orElse(null));
                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, null,0), "changeStatus-diagnosis");

                this.mqttService.sendNotificationClient("Tu dispositivo está siendo diagnosticado. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());


                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> diagnosisStatus (DiagnosisRepairDTO dto) {
        if(this.repairRepository.existsById(dto.getId())) {
            Repair repair = this.repairRepository.findById(dto.getId()).orElse(null);
            if(repair.getRepairStatus().getId() == 2) {
                repair.setRepairStatus(this.repairStatusRepository.findById(3L).orElse(null));
                repair.setDiagnostic_estimated_cost(dto.getDiagnostic_estimated_cost());
                repair.setDiagnostic_observations(dto.getDiagnostic_observations());
                repair.setDiagnostic_parts(dto.getDiagnostic_parts());
                Response<List<DiagnosticImage>> responseImages = this.imageService.SaveDiagnosticImage(dto.getDiagnostic_images(), repair.getId());

                if(responseImages.isError()) {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "Error saving images"
                    );
                }


                repair.setDiagnosticImage(responseImages.getData());

                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, null,0), "changeStatus-quotation");
                this.mqttService.sendNotificationAdmin("Nueva reparación lista para cotización. Número de reparación: " + repair.getId());
                this.mqttService.sendNotificationClient("La reparación de tu dispositivo está siendo realizada. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());

                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> quotationStatus (QuotationRepairDTO dto) {
        if(this.repairRepository.existsById(dto.getId())) {
            Repair repair = this.repairRepository.findById(dto.getId()).orElse(null);
            if(repair.getRepairStatus().getId() == 3) {
                repair.setRepairStatus(this.repairStatusRepository.findById(4L).orElse(null));
                double subtotal = dto.getHandwork_cost() + dto.getParts_cost();
                double total_price = subtotal + (subtotal * (dto.getProfit() * 0.01));
                repair.setTotal_price(total_price);

                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, repair.getDiagnostic_observations(), total_price), "changeStatus-quotation2");
                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-waitingforcustomerapproval");
                this.mqttService.sendNotificationClient("La cotización de tu dispositivo está lista, por favor revisala dentro de los próximos 5 días. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> customerApprovalStatus (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() == 4) {
                long newStatus = repair.getDiagnostic_parts().isEmpty() ? 6L : 5L;
                repair.setRepairStatus(this.repairStatusRepository.findById(newStatus).orElse(null));
                if(newStatus == 5) {
                    this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-waitingforparts");
                    this.mqttService.sendNotificationClient("Tu dispositivo está en espera de las partes necesarias para la reparación. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                } else {
                    repair.setRepair_start(LocalDateTime.now());
                    this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-repairing");
                    this.mqttService.sendNotificationClient("Tu dispositivo está siendo reparado. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                }
                this.mqttService.sendNotificationAdmin("El cliente ha aprobado la cotización de la reparación. Número de reparación: " + repair.getId());
                this.mqttService.sendNotificationTechnician("El cliente ha aprobado la cotización de la reparación. Número de reparación: " + repair.getId(), "/" + repair.getTechnician().getId());
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> customerRejectionStatus (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() == 4) {
                repair.setRepairStatus(this.repairStatusRepository.findById(7L).orElse(null));
                repair.setPaid(true);
                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-waitingforcollection");
                this.mqttService.sendNotificationAdmin("El cliente ha rechazado la cotización de la reparación. Número de reparación: " + repair.getId());
                this.mqttService.sendNotificationTechnician("El cliente ha rechazado la cotización de la reparación. Número de reparación: " + repair.getId(), "/" + repair.getTechnician().getId());
                this.mqttService.sendNotificationClient("Has rechazado la cotización para el dispositivo, por favor acude al centro de reparación para su entrega. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> startRepairStatus (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() == 5) {
                repair.setRepairStatus(this.repairStatusRepository.findById(6L).orElse(null));
                repair.setRepair_start(LocalDateTime.now());
                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-repairing");
                this.mqttService.sendNotificationClient("Tu dispositivo está siendo reparado. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else if (repair.getRepairStatus().getId() == 6) {
                return new Response<>(
                        null,
                        false,
                        200,
                        "The repair is already in progress"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> repairStatus (RepairRepairDTO dto) {
        if(this.repairRepository.existsById(dto.getId())) {
            Repair repair = this.repairRepository.findById(dto.getId()).orElse(null);
            if(repair.getRepairStatus().getId() == 6) {
                repair.setRepairStatus(this.repairStatusRepository.findById(7L).orElse(null));
                repair.setRepair_observations(dto.getRepair_observations());
                repair.setRepair_end(LocalDateTime.now());

                Response<List<RepairImage>> responseImages = this.imageService.SaveRepairImage(dto.getRepair_images(), repair.getId());

                if(responseImages.isError()) {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "Error saving images"
                    );
                }

                repair.setRepairImage(responseImages.getData());

                this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-waitingforcollection");
                this.mqttService.sendNotificationAdmin("La reparación ha finalizado. Número de reparación: " + repair.getId());
                this.mqttService.sendNotificationClient("Tu dispositivo ha sido reparado, por favor acude al centro de reparación para su entrega. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> collectedStatus (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() == 7) {
                if(repair.isPaid()) {
                    repair.setRepairStatus(this.repairStatusRepository.findById(8L).orElse(null));
                    this.emailService.sendMail(new EmailDto(repair.getClient().getEmail(), repair.getClient().getName(), "", repair.getDevice().toStringEmail() , null, "", 0), "changeStatus-collected");
                    this.mqttService.sendNotificationAdmin("El cliente ha recogido el dispositivo. Número de reparación: " + repair.getId());
                    this.mqttService.sendNotificationClient("Has recogido tu dispositivo, gracias por tu preferencia. Dispositivo : " + repair.getDevice().getBrand() + " - " + repair.getDevice().getModel() , "/" + repair.getClient().getId());
                    return new Response<>(
                            this.repairRepository.saveAndFlush(repair),
                            false,
                            200,
                            "OK"
                    );
                } else {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "The repair has not been paid"
                    );
                }
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response<Repair> payRepair (Long id) {
        if(this.repairRepository.existsById(id)) {
            Repair repair = this.repairRepository.findById(id).orElse(null);
            if(repair.getRepairStatus().getId() > 4) {
                repair.setPaid(true);
                return new Response<>(
                        this.repairRepository.saveAndFlush(repair),
                        false,
                        200,
                        "OK"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "The repair is not in the correct status"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Record not found"
            );
        }
    }




}
