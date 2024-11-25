package mx.edu.utez.sigser.controllers.repair.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiagnosisRepairDTO {

    @NotBlank(message = "Id is required")
    private Long id;

    @NotBlank(message = "Diagnostic observations is required")
    private String diagnostic_observations;

    private String diagnostic_parts;

    @NotBlank(message = "Diagnostic estimated cost is required")
    private double diagnostic_estimated_cost;


}
