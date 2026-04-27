package mart.repository;

import java.util.Optional;
import java.util.UUID;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mart.domain.entity.Inventory;
import mart.domain.entity.Status;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByName(String name);
    List<Inventory> findByStatus(Status status);
    
}
