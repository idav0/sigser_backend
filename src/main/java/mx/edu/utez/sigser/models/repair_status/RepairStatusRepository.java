package mx.edu.utez.sigser.models.repair_status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairStatusRepository extends JpaRepository<RepairStatus, Long> {

    Optional<RepairStatus> findById(Long id);
}
