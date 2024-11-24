package mx.edu.utez.sigser.models.diagnostic_image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticImageRepository extends JpaRepository<DiagnosticImage, Long> {
}
