package mx.edu.utez.sigser.controllers.device;

import com.google.zxing.WriterException;
import mx.edu.utez.sigser.controllers.device.dtos.CreateDeviceDTO;
import mx.edu.utez.sigser.models.device.Device;
import mx.edu.utez.sigser.services.device.DeviceService;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api-sigser/device")
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/")
    public ResponseEntity<Response<List<Device>>> getAll() {
        return new ResponseEntity<>(
                this.deviceService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Device>> getById(@PathVariable Long id) {
        Response<Device> response = this.deviceService.getById(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /*@PostMapping("/")
    public ResponseEntity<byte[]> generateQRCode(@RequestBody CreateDeviceDTO dto) {
        try {
            Response<byte[]> response= this.deviceService.create(dto);
            byte[] qrCode = response.getData();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");

            return new ResponseEntity<>(qrCode, headers, response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (WriterException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/")
    public ResponseEntity<Response<Device>> create(@RequestBody CreateDeviceDTO dto) {
        Response<Device> response = this.deviceService.create(dto.getDevice());
        return new ResponseEntity<>(
                response,
                response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );

    }

    @PutMapping("/")
    public ResponseEntity<Response<Device>> update(@RequestBody CreateDeviceDTO dto) {
        Response<Device> response = this.deviceService.update(dto.getDevice());
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Boolean>> delete(@PathVariable Long id) {
        Response<Boolean> response = this.deviceService.delete(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
