package com.vehicle.application.internal.commandservices;

import com.vehicle.domain.model.commands.ChangeVehicleMainImageCommand;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.vehicle.domain.exceptions.VehicleNotFoundException;
import com.vehicle.domain.exceptions.VehiclePlateAlreadyExistsException;
import com.vehicle.domain.model.aggregates.Vehicle;
import com.vehicle.domain.model.commands.ChangeVehicleStatusCommand;
import com.vehicle.domain.model.commands.CreateVehicleCommand;
import com.vehicle.domain.model.commands.UpdateVehicleDetailsCommand;
import com.vehicle.domain.model.events.VehicleCreatedEvent;
import com.vehicle.domain.model.events.VehicleUpdatedEvent;
import com.vehicle.domain.model.valueobjects.*;
import com.vehicle.infrastructure.messaging.VehicleEvents;
import com.vehicle.infrastructure.persistence.jpa.VehicleRepository;

import java.util.UUID;

@Service
@Transactional
public class VehicleCommandServiceImpl {

    private final VehicleRepository vehicleRepository;
    private final VehicleEvents eventProducer;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository,
                                     VehicleEvents eventProducer) {
        this.vehicleRepository = vehicleRepository;
        this.eventProducer = eventProducer;
    }

    public Long handle(CreateVehicleCommand command) {
        if (vehicleRepository.existsByPlate_Value(command.plate()))
            throw new VehiclePlateAlreadyExistsException(command.plate());

        var vehicle = Vehicle.builder()
                .plate(command.plate())
                .vin(command.vin())
                .brand(command.brand())
                .model(command.model())
                .year(command.year())
                .mileage(command.mileageKm())
                .price(command.priceAmount(), command.priceCurrency())
                .mainImageUrl(command.mainImageUrl())
                .build();

        var saved = vehicleRepository.save(vehicle);

        eventProducer.publishCreated(new VehicleCreatedEvent(
                saved.getId(), saved.getPlate().getValue(), saved.getVin().getValue()
        ));
        return saved.getId();
    }

    public void handle(UpdateVehicleDetailsCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));

        if (command.brand() != null) vehicle.setBrand(command.brand());
        if (command.model() != null) vehicle.setModel(command.model());
        if (command.year() != null) vehicle.setYear(new YearOfManufacture(command.year()));
        if (command.mileageKm() != null) vehicle.setMileage(new Mileage(command.mileageKm()));
        if (command.priceAmount() != null && command.priceCurrency() != null)
            vehicle.setPrice(new Money(command.priceAmount(), command.priceCurrency()));
        if (command.mainImageUrl() != null)
            vehicle.changeMainImageUrl(command.mainImageUrl());

        vehicleRepository.save(vehicle);
        eventProducer.publishUpdated(new VehicleUpdatedEvent(vehicle.getId()));
    }

    public void handle(ChangeVehicleStatusCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));

        vehicle.setStatus(command.newStatus());
        vehicleRepository.save(vehicle);
        eventProducer.publishUpdated(new VehicleUpdatedEvent(vehicle.getId()));
    }


    public void handle(ChangeVehicleMainImageCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));

        vehicle.changeMainImageUrl(command.mainImageUrl()); // null = quitar imagen
        vehicleRepository.save(vehicle);
        eventProducer.publishUpdated(new VehicleUpdatedEvent(vehicle.getId()));
    }





}