package com.vehicle.infrastructure.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vehicle.domain.model.aggregates.Vehicle;
import com.vehicle.domain.model.valueobjects.VehicleStatus;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate_Value(String plate);
    Optional<Vehicle> findByVin_Value(String vin);
    boolean existsByPlate_Value(String plate);
    boolean existsByVin_Value(String vin);
    Page<Vehicle> findByStatus(VehicleStatus status, Pageable pageable);

}


