package mx.edu.utez.sigser.models.user_type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sigser.models.user.User;

import java.util.List;

@Entity
@Table(name = "user_types")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;

    //SuperAdmin, Admin, Technician, Client


    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;


}
