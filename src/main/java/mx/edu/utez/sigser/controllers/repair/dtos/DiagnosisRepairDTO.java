package mx.edu.utez.sigser.controllers.repair.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiagnosisRepairDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Diagnostic observations is required")
    private String diagnostic_observations;

    private String diagnostic_parts;

    @NotNull(message = "Diagnostic estimated cost is required")
    private double diagnostic_estimated_cost;

    @NotEmpty(message = "Diagnostic images are required, at least one image is required")
    List<String> diagnostic_images;


}
