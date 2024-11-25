package mx.edu.utez.sigser.models.repair;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    @Query(value = "SELECT * FROM repairs WHERE client_id = :userId", nativeQuery = true)
    List<Repair> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM repairs WHERE device_id = :deviceId AND repair_status_id != 8", nativeQuery = true)
    List<Repair> findAllByDeviceIdAndDifferentRepairStatusCollected(Long deviceId);


}
