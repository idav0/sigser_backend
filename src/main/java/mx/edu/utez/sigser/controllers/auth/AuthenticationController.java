package mx.edu.utez.sigser.controllers.auth;

import jakarta.annotation.security.PermitAll;
import mx.edu.utez.sigser.controllers.auth.dtos.LoginResponseDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.LoginUserDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.RegisterUserDTO;
import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.services.JWTService;
import mx.edu.utez.sigser.services.auth.AuthenticationService;
import mx.edu.utez.sigser.utils.LoginResponse;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JWTService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<Object>> register(@RequestBody RegisterUserDTO registerUserDto) {
        Response<Object> response = authenticationService.signup(registerUserDto);

        return new ResponseEntity<>(
                response,
                response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/login")

    public ResponseEntity<Response<Object>> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        Object userWithToken = new Object() {
            public final LoginResponseDTO userInfo = new LoginResponseDTO(authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getLastname(), authenticatedUser.getPhone(), authenticatedUser.getEmail(), authenticatedUser.getAuthorities());
            public final LoginResponse loginInfo = loginResponse;
        };

        return new ResponseEntity<>(
                new Response<>(
                        userWithToken,
                        false,
                        200,
                        "OK"
                ),
                HttpStatus.OK
        );
    }}