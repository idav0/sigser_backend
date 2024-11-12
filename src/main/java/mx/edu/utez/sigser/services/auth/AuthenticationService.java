package mx.edu.utez.sigser.services.auth;

import mx.edu.utez.sigser.controllers.auth.dtos.LoginUserDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.RegisterUserDTO;
import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.models.user.UserRepository;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Response<Object> signup(RegisterUserDTO input) {
        if (input.getEmail() == null || input.getPassword() == null ||  input.getName() == null || input.getLastname() == null || input.getPhone() == null) {
            return new Response<>(
                    null,
                    true,
                    400,
                    "All fields are required"
            );
        }
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            return new Response<>(
                    null,
                    true,
                    400,
                    "Email already exists"
            );
        }
        User user = new User(
                input.getName(),
                input.getLastname(),
                input.getPhone(),
                input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                input.getUserType()
        );
        userRepository.saveAndFlush(user);
        return new Response<>(
                null,
                false,
                201,
                "User created successfully"
        );
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}