package mx.edu.utez.sigser.controllers.repair.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.device.Device;
import mx.edu.utez.sigser.models.diagnostic_image.DiagnosticImage;
import mx.edu.utez.sigser.models.repair.Repair;
import mx.edu.utez.sigser.models.repair_image.RepairImage;
import mx.edu.utez.sigser.models.repair_status.RepairStatus;
import mx.edu.utez.sigser.models.user.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRepairDTO {

    private Long id;

    private String problem_description;

    private LocalDateTime entry_date;

    private String diagnostic_observations;

    private String diagnostic_parts;

    double diagnostic_estimated_cost;

    double total_price;

    private String repair_observations;

    private LocalDateTime repair_start;

    private LocalDateTime repair_end;

    private boolean paid = false;


    private User client;

    private User technician;

    private RepairStatus repairStatus;

    private Device device;

    private List<DiagnosticImage> diagnosticImage;

    private List<RepairImage> repairImage;

    public Repair getRepair() {
        return new Repair(
                this.id,
                this.problem_description,
                this.entry_date,
                this.diagnostic_observations,
                this.diagnostic_parts,
                this.diagnostic_estimated_cost,
                this.total_price,
                this.repair_observations,
                this.repair_start,
                this.repair_end,
                this.paid,
                this.client,
                this.technician,
                this.repairStatus,
                this.device,
                this.diagnosticImage,
                this.repairImage
        );
    }


}
