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
public class QuotationRepairDTO {

        @NotBlank(message = "Id is required")
        private Long id;

        @NotBlank(message = "Handwork cost is required")
        private double handwork_cost;

        @NotBlank(message = "Parts cost is required, if there are no parts, put 0")
        private double parts_cost;

        @NotBlank(message = "Profit is required in percentage")
        private int profit;

}
