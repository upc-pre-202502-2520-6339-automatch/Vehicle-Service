package com.vehicle.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class Vin {
    private String value;

    protected Vin() {}
    public Vin(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("VIN required");
        this.value = value.toUpperCase();
    }
    public String getValue() { return value; }
    @Override public boolean equals(Object o){ return o instanceof Vin v && Objects.equals(value, v.value);}
    @Override public int hashCode(){ return Objects.hash(value); }
    @Override public String toString(){ return value; }
}