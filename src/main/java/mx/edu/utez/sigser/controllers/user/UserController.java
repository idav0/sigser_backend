package mx.edu.utez.sigser.controllers.user;

import mx.edu.utez.sigser.models.user.User;
import mx.edu.utez.sigser.services.user.UserService;
import mx.edu.utez.sigser.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-sigser/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public ResponseEntity<Response<List<User>>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/admins")
    public ResponseEntity<Response<List<User>>> getAllAdmins() {
        return new ResponseEntity<>(userService.findAllAdmins(), HttpStatus.OK);
    }

    @GetMapping("/technicians")
    public ResponseEntity<Response<List<User>>> getAllTechnicians() {
        return new ResponseEntity<>(userService.findAllTechnicians(), HttpStatus.OK);
    }

    @GetMapping("/clients")
    public ResponseEntity<Response<List<User>>> getAllClients() {
        return new ResponseEntity<>(userService.findAllClients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<User>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }
}
