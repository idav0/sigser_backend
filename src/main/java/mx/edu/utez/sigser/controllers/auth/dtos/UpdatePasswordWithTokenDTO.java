package mx.edu.utez.sigser.controllers.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePasswordWithTokenDTO {

    private String email;
    private String token;
    private String newPassword;

}
