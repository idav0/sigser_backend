package mx.edu.utez.sigser.models.diagnostic_image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosticImageRepository extends JpaRepository<DiagnosticImage, Long> {

    Optional<DiagnosticImage> findById(Long id);
}
