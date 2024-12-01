package mx.edu.utez.sigser.services.auth;

import mx.edu.utez.sigser.controllers.auth.dtos.LoginUserDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.RegisterUserDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.UpdateNewUserPasswordDTO;
import mx.edu.utez.sigser.controllers.auth.dtos.UpdatePasswordWithTokenDTO;
import mx.edu.utez.sigser.controllers.email.dto.EmailDto;
import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.models.user.UserRepository;
import mx.edu.utez.sigser.models.user_type.UserType;
import mx.edu.utez.sigser.models.user_type.UserTypeRepository;
import mx.edu.utez.sigser.utils.EmailService;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final UserTypeRepository userTypeRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            UserTypeRepository userTypeRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userTypeRepository = userTypeRepository;

    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<User> signup(RegisterUserDTO input, String userTypeName) {
        if (input.getEmail() == null || input.getName() == null || input.getLastname() == null || input.getPhone() == null) {
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
        String tmp_password = createToken(12);
        UserType userType = userTypeRepository.findByName(userTypeName);
        if (userType == null) {
            return new Response<>(
                    null,
                    true,
                    400,
                    "User type not found"
            );
        }
        User user = new User(
                input.getName(),
                input.getLastname(),
                input.getPhone(),
                input.getEmail(),
                passwordEncoder.encode(tmp_password),
                userType
        );

        this.emailService.sendMail(new EmailDto(input.getEmail(), input.getName(), "", "",tmp_password,"",0), "newUser");
        return new Response<>(
                this.userRepository.saveAndFlush(user),
                false,
                201,
                "User created successfully"
        );
    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<Object> updateNewUserPassword(UpdateNewUserPasswordDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
            if (user.isNew_user()) {
                if (!checkPassword(dto.getNewPassword())) {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "Password must be at least 8 characters long, have at least one uppercase letter  and one number and not contain spaces"
                    );
                }
                if (userRepository.updateUserPasswordByEmail(passwordEncoder.encode(dto.getNewPassword()), dto.getEmail()) > 0) {
                    userRepository.updateNoNewUserByEmail( dto.getEmail());
                    return new Response<>(
                            null,
                            false,
                            200,
                            "Password updated successfully"
                    );
                } else {
                    return new Response<>(
                            null,
                            true,
                            500,
                            "Internal server error updating password"
                    );
                }
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "User is not new"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "User not found"
            );
        }
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();

        if (user.isActive()) {
            if (!user.isNew_user()) {
                return user;
            } else {
                throw new CredentialsExpiredException("User must change password");
            }
        } else {
            throw new DisabledException("User is not active");
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<Object> forgotPassword(LoginUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            String newToken = createToken(6);
            ArrayList<String> lista = new ArrayList<>();

            if (userRepository.updateUserTokenTmpByEmail(newToken, dto.getEmail()) > 0) {
                lista.add("Token updated successfully");

                if (this.emailService.sendMail(new EmailDto(dto.getEmail(), "", newToken,"","","",0), "forgotPassword")) {
                    lista.add("Email sent successfully");
                    return new Response<>(
                            lista.toArray(),
                            false,
                            200,
                            "OK"
                    );
                } else {
                    lista.add("Something went wrong sending the email");
                    return new Response<>(
                            lista.toArray(),
                            true,
                            500,
                            "Internal server error sending email"
                    );
                }
            } else {
                lista.add("Something went wrong updating the token");
                return new Response<>(
                        lista.toArray(),
                        true,
                        500,
                        "Internal server error updating token"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "The email does not exist"
            );
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public Response<Object> updatePasswordWithToken(UpdatePasswordWithTokenDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
            if (user.getToken_tmp().equals(dto.getToken())) {
                if (checkPassword(dto.getNewPassword())) {
                    return new Response<>(
                            null,
                            true,
                            400,
                            "Password must be at least 8 characters long, have at least one uppercase letter, one special character, one number and not contain spaces"
                    );
                }
                if (userRepository.updateUserPasswordByEmail(passwordEncoder.encode(dto.getNewPassword()), dto.getEmail()) > 0) {
                    userRepository.updateUserTokenTmpByEmail(null, dto.getEmail());
                    return new Response<>(
                            null,
                            false,
                            200,
                            "Password updated successfully"
                    );
                } else {
                    return new Response<>(
                            null,
                            true,
                            500,
                            "Internal server error updating password"
                    );
                }
            } else {
                return new Response<>(
                        null,
                        true,
                        400,
                        "Token is invalid"
                );
            }
        } else {
            return new Response<>(
                    null,
                    true,
                    400,
                    "User not found"
            );
        }
    }


    private String createToken(int length) {
        {
            StringBuilder token = new StringBuilder();
            String[] caracteres = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A",
                    "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                    "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
            for (int i = 0; i < length; i++) {
                token.append(caracteres[(int) (Math.random() * 60)]);
            }
            return token.toString();
        }

    }

    public boolean checkPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        return hasUppercase && hasNumber;

    }



}