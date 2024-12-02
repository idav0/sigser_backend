package mx.edu.utez.sigser.controllers.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api-sigser/images")
public class ImageController {

    private static final String PATH_IMG_DIAGNOSIS = "src/main/resources/static/images/evidence/diagnosis";
    private static final String PATH_IMG_REPAIR = "src/main/resources/static/images/evidence/repair";


    @GetMapping(value = "/diagnosis/{img}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImageUser(@PathVariable("img") String img) {
        Path imagePath = Paths.get(PATH_IMG_DIAGNOSIS + "/" + img);
        Resource resource = new FileSystemResource(imagePath.toFile());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/repair/{img}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImageService(@PathVariable("img") String img) {
        Path imagePath = Paths.get(PATH_IMG_REPAIR + "/" + img);
        Resource resource = new FileSystemResource(imagePath.toFile());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
