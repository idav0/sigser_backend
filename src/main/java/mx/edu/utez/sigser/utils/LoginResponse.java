package mx.edu.utez.sigser.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @Getter
    private String token;

    private long expiresIn;

    // Getters and setters...
}