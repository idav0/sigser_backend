package mx.edu.utez.sigser.models.diagnostic_image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.repair.Repair;

@Entity
@Table(name = "diagnostic_images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiagnosticImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "repair_id")
    @JsonIgnore
    private Repair repair;

}
