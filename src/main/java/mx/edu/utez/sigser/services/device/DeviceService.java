package mx.edu.utez.sigser.services.device;

import mx.edu.utez.sigser.controllers.device.dtos.CreateDeviceDTO;
import mx.edu.utez.sigser.models.device.Device;
import mx.edu.utez.sigser.models.device.DeviceRepository;
import mx.edu.utez.sigser.models.device_type.DeviceTypeRepository;
import mx.edu.utez.sigser.models.user.UserRepository;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import java.sql.SQLException;
import java.util.List;


@Service
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final UserRepository userRepository;

    public DeviceService(DeviceRepository deviceRepository, DeviceTypeRepository deviceTypeRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.userRepository = userRepository;

    }



    @Transactional(readOnly = true)
    public Response<List<Device>> getAll() {
        return new Response<>(
                this.deviceRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<Device> getById(Long id) {
        if(this.deviceRepository.existsById(id)) {
            return new Response<>(
                    this.deviceRepository.findById(id).orElse(null),
                    false,
                    200,
                    "OK"
            );
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "The Device does not exist"
            );
        }
    }


   /* @Transactional
    public Response<byte[]> create(CreateDeviceDTO device) throws WriterException {
        if (this.deviceRepository.findBySerialNumber(device.getSerialNumber()).isEmpty()) {

            Device newDevice = this.deviceRepository.saveAndFlush(device.getDevice());
            newDevice.setDeviceType(this.deviceTypeRepository.findById(newDevice.getDeviceType().getId()).orElse(null));


            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(newDevice.toString(), BarcodeFormat.QR_CODE, 300, 300);
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
                    "The Device was created successfully"
            );


        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "The Device already exists"
            );
        }
    }*/

    @Transactional(rollbackFor = SQLException.class)
    public Response<Device> create(Device device) {
        if (this.deviceRepository.findBySerialNumber(device.getSerialNumber()).isEmpty()) {
            device.setDeviceType(this.deviceTypeRepository.findById(device.getDeviceType().getId()).orElse(null));
            return new Response<>(
                    this.deviceRepository.saveAndFlush(device),
                    false,
                    201,
                    "The Device was created successfully"
            );
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "The Device already exists"
            );
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<Device> update (Device device) {
        if (this.deviceRepository.existsById(device.getId())) {
            device.setDeviceType(this.deviceTypeRepository.findById(device.getDeviceType().getId()).orElse(null));
            return new Response<>(
                    this.deviceRepository.saveAndFlush(device),
                    false,
                    200,
                    "The Device was updated successfully"
            );
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "The Device does not exist"
            );
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<Boolean> delete (Long id) {
        if (this.deviceRepository.existsById(id)) {
            this.deviceRepository.deleteById(id);
            return new Response<>(
                    true,
                    false,
                    200,
                    "The Device was deleted successfully"
            );
        } else {
            return new Response<>(
                    false,
                    true,
                    400,
                    "The Device does not exist"
            );
        }
    }



}



