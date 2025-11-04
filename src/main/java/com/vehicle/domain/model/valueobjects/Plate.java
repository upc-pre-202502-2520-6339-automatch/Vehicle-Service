package com.vehicle.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Embeddable
public class Plate {
    private String value;

    protected Plate() {}
    public Plate(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Plate required");
        this.value = value.toUpperCase();
    }
    public String getValue() { return value; }

    @Override public boolean equals(Object o){ return o instanceof Plate p && Objects.equals(value, p.value);}
    @Override public int hashCode(){ return Objects.hash(value); }
    @Override public String toString(){ return value; }
}