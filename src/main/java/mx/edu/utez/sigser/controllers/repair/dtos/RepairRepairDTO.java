package mx.edu.utez.sigser.controllers.repair.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepairRepairDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Repair observations is required")
    private String repair_observations;

}


