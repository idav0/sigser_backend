package mx.edu.utez.sigser.controllers.auth.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;

}
