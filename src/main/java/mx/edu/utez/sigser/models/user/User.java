package mx.edu.utez.sigser.models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import mx.edu.utez.sigser.models.repair.Repair;
import mx.edu.utez.sigser.models.user_type.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45)
    private String lastname;

    @Column(nullable = false, length = 12)
    private String phone;

    @Column(nullable = false, length = 45, unique = true)
    private String email;

    @Column(nullable = false, length = 256)
    private String password;

    @Column
    private String token_tmp;

    @Column(nullable = false)
    private boolean new_user = true;

    @Column(nullable = false)
    private boolean active = true;


    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Repair> repairsClient;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Repair> repairsTechnician;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.getName()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


    //SuperAdmin, Admin, Technician, Client



    public User(String name, String lastname, String phone, String email, String password, UserType userType) {
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }


}
