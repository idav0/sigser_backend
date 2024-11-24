package mx.edu.utez.sigser.models.repair_image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairImageRepository extends JpaRepository<RepairImage, Long> {
}
