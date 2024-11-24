package mx.edu.utez.sigser.models.device_type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.device.Device;

@Entity
@Table(name = "device_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;


    @OneToOne(mappedBy = "deviceType", cascade = CascadeType.ALL)
    @JsonIgnore
    private Device device;

}
