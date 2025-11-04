package com.vehicle.application.internal.queryservices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.vehicle.domain.exceptions.VehicleNotFoundException;
import com.vehicle.domain.model.aggregates.Vehicle;
import com.vehicle.domain.model.queries.GetVehicleByIdQuery;
import com.vehicle.domain.model.queries.GetVehiclesByFiltersQuery;
import com.vehicle.domain.model.valueobjects.VehicleStatus;
import com.vehicle.infrastructure.persistence.jpa.VehicleRepository;

import java.util.List;

@Service
public class VehicleQueryServiceImpl {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(query.vehicleId()));
    }

    public Page<Vehicle> handle(GetVehiclesByFiltersQuery query, Pageable pageable) {
        if (query.status() != null) {
            return vehicleRepository.findByStatus(query.status(), pageable);
        }
        return vehicleRepository.findAll(pageable);
    }
}