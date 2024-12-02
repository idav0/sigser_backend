package mx.edu.utez.sigser.models.repair_image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairImageRepository extends JpaRepository<RepairImage, Long> {

    Optional<RepairImage> findById(Long id);
}
