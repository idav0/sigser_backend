package mx.edu.utez.sigser.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsById (Long id);

    boolean existsByEmail(String email);

    @Modifying
    @Query(
            value = "UPDATE users u SET password = :password WHERE email = :email",
            nativeQuery = true
    )
    int updateUserPasswordByEmail(String password, String email);

    @Modifying
    @Query(
            value = "UPDATE users u SET token_tmp = :token WHERE email = :email",
            nativeQuery = true
    )
    int updateUserTokenTmpByEmail(String token, String email);

    @Modifying
    @Query(
            value = "UPDATE users u SET active = :active WHERE email = :email",
            nativeQuery = true
    )
    int updateUserActiveByEmail(boolean active, String email);

    @Modifying
    @Query(
            value = "UPDATE users u SET new_user = false WHERE email = :email",
            nativeQuery = true
    )
    int updateNoNewUserByEmail(String email);

    @Query(
            value = "SELECT * FROM users WHERE user_type_id = 2",
            nativeQuery = true
    )
    List<User> findAllAdmins();

    @Query(
            value = "SELECT * FROM users WHERE user_type_id = 3",
            nativeQuery = true
    )
    List<User> findAllTechnicians();

    @Query(
            value = "SELECT * FROM users WHERE user_type_id = 4",
            nativeQuery = true
    )
    List<User> findAllClients();

}
