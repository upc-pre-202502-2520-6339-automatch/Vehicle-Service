package com.vehicle.domain.exceptions;

public class VehicleDeletionNotAllowedException extends RuntimeException {
    public VehicleDeletionNotAllowedException(String msg) { super(msg); }
}

