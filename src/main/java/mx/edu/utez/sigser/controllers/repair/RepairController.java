package mx.edu.utez.sigser.controllers.repair;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import mx.edu.utez.sigser.controllers.repair.dtos.CreateRepairDTO;
import mx.edu.utez.sigser.controllers.repair.dtos.DiagnosisRepairDTO;
import mx.edu.utez.sigser.controllers.repair.dtos.QuotationRepairDTO;
import mx.edu.utez.sigser.controllers.repair.dtos.RepairRepairDTO;
import mx.edu.utez.sigser.models.repair.Repair;
import mx.edu.utez.sigser.services.repair.RepairService;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api-sigser/repair")
@RestController
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/")
    public ResponseEntity<Response<List<Repair>>> getAll() {
        return new ResponseEntity<>(
                this.repairService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Repair>> getById(@PathVariable Long id) {
        Response<Repair> response = this.repairService.getById(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Response<List<Repair>>> getByClientId(@PathVariable Long id) {
        Response<List<Repair>> response = this.repairService.getAllByClientId(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/technician/{id}")
    public ResponseEntity<Response<List<Repair>>> getByTechnicianId(@PathVariable Long id) {
        Response<List<Repair>> response = this.repairService.getAllByTechnicianId(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/")
    public ResponseEntity<byte[]> create(@RequestBody CreateRepairDTO dto) {
        try {
            Response<byte[]> response= this.repairService.receivedStatus(dto.getRepair());
            byte[] qrCode = response.getData();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");

            return new ResponseEntity<>(qrCode, headers, response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (WriterException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/status/start-diagnostic/{id}")
    public ResponseEntity<Response<Repair>> startDiagnostic(@PathVariable Long id) {
        Response<Repair> response = this.repairService.startDiagnosisStatus(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/end-diagnostic")
    public ResponseEntity<Response<Repair>> endDiagnostic(@RequestBody @Valid DiagnosisRepairDTO dto) {
        Response<Repair> response = this.repairService.diagnosisStatus(dto);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/quotation")
    public ResponseEntity<Response<Repair>> quotation(@RequestBody @Valid QuotationRepairDTO dto) {
        Response<Repair> response = this.repairService.quotationStatus(dto);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/customer-approval/{id}")
    public ResponseEntity<Response<Repair>> customerApproval(@PathVariable Long id) {
        Response<Repair> response = this.repairService.customerApprovalStatus(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/customer-rejection/{id}")
    public ResponseEntity<Response<Repair>> customerRejection(@PathVariable Long id) {
        Response<Repair> response = this.repairService.customerRejectionStatus(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/start-repair/{id}")
    public ResponseEntity<Response<Repair>> startRepair(@PathVariable Long id) {
        Response<Repair> response = this.repairService.startRepairStatus(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/end-repair")
    public ResponseEntity<Response<Repair>> endRepair(@RequestBody @Valid RepairRepairDTO dto) {
        Response<Repair> response = this.repairService.repairStatus(dto);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/collection/{id}")
    public ResponseEntity<Response<Repair>> collection(@PathVariable Long id) {
        Response<Repair> response = this.repairService.collectedStatus(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/status/paid/{id}")
    public ResponseEntity<Response<Repair>> paid(@PathVariable Long id) {
        Response<Repair> response = this.repairService.payRepair(id);
        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }



}
