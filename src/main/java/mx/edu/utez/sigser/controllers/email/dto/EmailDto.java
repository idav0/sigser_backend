package mx.edu.utez.sigser.controllers.email.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailDto {
    private String email;
    private String nombre;
    private String token;
    private String equipo;
    private String tmpcontra;
    private String descripcion;
    private double monto;

}
