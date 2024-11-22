package mx.edu.utez.sigser.controllers.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewUserPasswordDTO {

    private String email;
    private String newPassword;
    private String oldPassword;

}
