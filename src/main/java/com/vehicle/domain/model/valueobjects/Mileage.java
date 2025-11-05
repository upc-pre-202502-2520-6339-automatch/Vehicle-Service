package com.vehicle.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Embeddable
public class Mileage {
    private int kilometers;
    protected Mileage(){}
    public Mileage(int kilometers){
        if (kilometers < 0) throw new IllegalArgumentException("km >= 0");
        this.kilometers = kilometers;
    }
    public int getKilometers(){ return kilometers; }
}