package mx.edu.utez.sigser.controllers.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.device.Device;
import mx.edu.utez.sigser.models.device_type.DeviceType;
import mx.edu.utez.sigser.models.repair.Repair;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDeviceDTO {

    private Long id;
    private String model;
    private String brand;
    private String serialNumber;
    private DeviceType deviceType;

    private List<Repair> repairs;

    public Device getDevice() {
        return new Device(
            this.id,
            this.model,
            this.brand,
            this.serialNumber,
            this.deviceType,
            this.repairs
        );
    }

}
