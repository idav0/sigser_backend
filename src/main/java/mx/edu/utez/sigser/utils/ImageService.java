package mx.edu.utez.sigser.utils;

import mx.edu.utez.sigser.models.diagnostic_image.DiagnosticImage;
import mx.edu.utez.sigser.models.diagnostic_image.DiagnosticImageRepository;
import mx.edu.utez.sigser.models.repair.Repair;
import mx.edu.utez.sigser.models.repair.RepairRepository;
import mx.edu.utez.sigser.models.repair_image.RepairImage;
import mx.edu.utez.sigser.models.repair_image.RepairImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.tools.Diagnostic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImageService {


    private final RepairImageRepository repairImageRepository;
    private final DiagnosticImageRepository diagnosticImageRepository;
    private final RepairRepository repairRepository;

    public ImageService(RepairImageRepository repairImageRepository, DiagnosticImageRepository diagnosticImageRepository, RepairRepository repairRepository) {
        this.repairImageRepository = repairImageRepository;
        this.diagnosticImageRepository = diagnosticImageRepository;
        this.repairRepository = repairRepository;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Response<List<RepairImage>> SaveRepairImage(List<String> images, Long repairId) {

        Repair repair = this.repairRepository.findById(repairId).orElse(null);
        if (repair != null) {
            List<RepairImage> imagesSaved = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
                String imageName = repairId + "_" + i + ".jpg";
                RepairImage repairImage = new RepairImage();

                try {
                    byte[] imageBytes = java.util.Base64.getDecoder().decode(images.get(i));
                    String REPAIR_PATH = "src/main/resources/static/images/evidence/repair/";
                    Path imagePathPerImage = Paths.get(REPAIR_PATH + imageName);
                    Files.write(imagePathPerImage, imageBytes);

                    repairImage.setName(imageName);
                    repairImage.setRepair(this.repairRepository.findById(repairId).orElse(null));
                    RepairImage repairImageSaved = this.repairImageRepository.saveAndFlush(repairImage);
                    imagesSaved.add(repairImageSaved);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            if (!imagesSaved.isEmpty()) {
                return new Response<>(
                        imagesSaved,
                        false,
                        200,
                        "Images saved successfully"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "Error saving images"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Repair not found"
            );
        }
    }


    @Transactional(rollbackFor = SQLException.class)
    public Response<List<DiagnosticImage>> SaveDiagnosticImage(List<String> images, Long repairId) {

        Repair repair = this.repairRepository.findById(repairId).orElse(null);
        if (repair != null) {
            List<DiagnosticImage> imagesSaved = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
                String imageName = repairId + "_" + i + ".jpg";
                DiagnosticImage diagnosticImage = new DiagnosticImage();

                try {
                    byte[] imageBytes = java.util.Base64.getDecoder().decode(images.get(i));
                    String DIAGNOSIS_PATH = "src/main/resources/static/images/evidence/diagnosis/";
                    Path imagePathPerImage = Paths.get(DIAGNOSIS_PATH + imageName);
                    Files.write(imagePathPerImage, imageBytes);

                    diagnosticImage.setName(imageName);
                    diagnosticImage.setRepair(this.repairRepository.findById(repairId).orElse(null));
                    DiagnosticImage diagnosticImageSaved = this.diagnosticImageRepository.saveAndFlush(diagnosticImage);
                    imagesSaved.add(diagnosticImageSaved);


                } catch (IOException e) {
                    e.printStackTrace();
                    return new Response<>(null, true, 400, "Error saving images");
                }

            }


            if (!imagesSaved.isEmpty()) {
                return new Response<>(
                        imagesSaved,
                        false,
                        200,
                        "Images saved successfully"
                );
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "Error saving images"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Repair not found"
            );
        }
    }



}
