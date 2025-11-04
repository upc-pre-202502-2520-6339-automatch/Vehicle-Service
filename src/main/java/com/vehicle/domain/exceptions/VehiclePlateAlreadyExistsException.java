package com.vehicle.domain.exceptions;

public class VehiclePlateAlreadyExistsException extends RuntimeException {
    public VehiclePlateAlreadyExistsException(String plate){ super("Plate already registered: "+plate); }
}