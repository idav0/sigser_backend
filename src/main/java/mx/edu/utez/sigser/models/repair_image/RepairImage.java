package mx.edu.utez.sigser.models.repair_image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.repair.Repair;

@Entity
@Table(name = "repair_images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepairImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "repair_id")
    @JsonIgnore
    private Repair repair;


}
