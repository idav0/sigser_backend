package mx.edu.utez.sigser.controllers.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.models.user_type.UserType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {

    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String password;

    private UserType userType;

}
