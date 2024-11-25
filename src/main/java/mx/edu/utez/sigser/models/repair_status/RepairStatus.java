package mx.edu.utez.sigser.models.repair_status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.repair.Repair;

import java.util.List;

@Entity
@Table(name = "repair_statuses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepairStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;


    @OneToMany(mappedBy = "repairStatus", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Repair> repair;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
