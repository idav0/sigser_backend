package mx.edu.utez.sigser.models.repair;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.device.Device;
import mx.edu.utez.sigser.models.diagnostic_image.DiagnosticImage;
import mx.edu.utez.sigser.models.repair_image.RepairImage;
import mx.edu.utez.sigser.models.repair_status.RepairStatus;
import mx.edu.utez.sigser.models.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "repairs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String problem_description;

    @Column(nullable = false)
    private LocalDateTime entry_date;

    @Column(length = 300)
    private String diagnostic_observations;

    @Column(length = 300)
    private String diagnostic_parts;

    @Column
    double diagnostic_estimated_cost;

    @Column
    double total_price;

    @Column(length = 300)
    private String repair_observations;

    @Column
    private LocalDateTime repair_start;

    @Column
    private LocalDateTime repair_end;

    @Column
    private boolean paid = false;


    @ManyToOne
    @JoinColumn(name="client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name="technician_id")
    private User technician;

    @ManyToOne
    @JoinColumn(name="repair_status_id")
    private RepairStatus repairStatus;

    @ManyToOne
    @JoinColumn(name="device_id")
    private Device device;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    private List<DiagnosticImage> diagnosticImage;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    private List<RepairImage> repairImage;


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", problem_description='" + problem_description + '\'' +
                ", entry_date='" + entry_date + '\'' +
                ", device=" + device.toString() +
                '}';
    }



}
