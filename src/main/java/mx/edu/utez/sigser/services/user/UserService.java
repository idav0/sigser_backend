package mx.edu.utez.sigser.services.user;

import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.models.user.UserRepository;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Response<List<User>> findAll() {
        return new Response<>(
                this.userRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<User> findById(Long id) {
        if(this.userRepository.existsById(id)) {
            return new Response<>(
                    this.userRepository.findById(id).get(),
                    false,
                    200,
                    "OK"
            );

        } else {
            return new Response<>(
                    null,
                    true,
                    404,
                    "User not found"
            );
        }
    }

    @Transactional(readOnly = true)
    public Response<List<User>> findAllAdmins() {
        return new Response<>(
                this.userRepository.findAllAdmins(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<List<User>> findAllTechnicians() {
        return new Response<>(
                this.userRepository.findAllTechnicians(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<List<User>> findAllClients() {
        return new Response<>(
                this.userRepository.findAllClients(),
                false,
                200,
                "OK"
        );
    }






}
