package mx.edu.utez.sigser.controllers.auth;

import jakarta.annotation.security.PermitAll;
import mx.edu.utez.sigser.controllers.auth.dtos.*;
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

@RequestMapping("/api-sigser/auth")
@RestController
public class AuthenticationController {
    private final JWTService jwtService;

    private final AuthenticationService authenticationService;

    //TODO : Test all this endpoints once the email templates are ready

    public AuthenticationController(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<Response<User>> register(@RequestBody RegisterUserDTO registerUserDto) {
        Response<User> response = authenticationService.signup(registerUserDto, "ADMIN");

        return new ResponseEntity<>(
                response,
                response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/signup/technician")
    public ResponseEntity<Response<User>> registerTechnician(@RequestBody RegisterUserDTO registerUserDto) {
        Response<User> response = authenticationService.signup(registerUserDto, "TECHNICIAN");

        return new ResponseEntity<>(
                response,
                response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/signup/client")
    public ResponseEntity<Response<User>> registerClient(@RequestBody RegisterUserDTO registerUserDto) {
        Response<User> response = authenticationService.signup(registerUserDto, "CLIENT");

        return new ResponseEntity<>(
                response,
                response.getStatus() == 201 ? HttpStatus.CREATED : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/signup/update-password")
    public ResponseEntity<Response<Object>> updateNewUserPassword (@RequestBody UpdateNewUserPasswordDTO updateNewUserPasswordDto) {
        Response<Object> response = authenticationService.updateNewUserPassword(updateNewUserPasswordDto);

        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
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
    }

    @PostMapping("/forgot-password/token")
    public ResponseEntity<Response<Object>> forgotPassword(@RequestBody LoginUserDTO dto) {
        Response<Object> response = authenticationService.forgotPassword(dto);

        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/forgot-password/update-password")
    public ResponseEntity<Response<Object>> updatePasswordWithToken(@RequestBody UpdatePasswordWithTokenDTO updatepasswordWithTokenDto) {
        Response<Object> response = authenticationService.updatePasswordWithToken(updatepasswordWithTokenDto);

        return new ResponseEntity<>(
                response,
                response.getStatus() == 200 ? HttpStatus.OK : response.getStatus() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }




}

