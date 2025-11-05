package com.vehicle.domain.model.valueobjects;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Embeddable
public class YearOfManufacture {
    private int year;
    protected YearOfManufacture(){}
    public YearOfManufacture(int year){
        if (year < 1950 || year > 2100) throw new IllegalArgumentException("invalid year");
        this.year = year;
    }
}