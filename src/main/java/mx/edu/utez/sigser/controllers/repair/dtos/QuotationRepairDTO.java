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
public class QuotationRepairDTO {

        @NotNull(message = "Id is required")
        private Long id;

        @NotNull(message = "Handwork cost is required")
        private double handwork_cost;

        @NotNull(message = "Parts cost is required, if there are no parts, put 0")
        private double parts_cost;

        @NotNull(message = "Profit is required in percentage")
        private int profit;

}
