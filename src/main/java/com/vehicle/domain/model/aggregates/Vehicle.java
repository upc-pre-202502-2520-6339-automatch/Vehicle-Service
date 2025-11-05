package com.vehicle.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.vehicle.domain.model.valueobjects.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "vehicles",
        indexes = {
                @Index(name = "uk_vehicle_plate", columnList = "plate_value", unique = true),
                @Index(name = "uk_vehicle_vin",   columnList = "vin_value",   unique = true)
        })
public class Vehicle {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Getter
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


    @Embedded
    @AttributeOverrides(@AttributeOverride(name="value", column=@Column(name="plate_value", nullable=false)))
    private Plate plate;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name="value", column=@Column(name="vin_value", nullable=false)))
    private Vin vin;

    private String brand;
    private String model;

    @Embedded private YearOfManufacture year;
    @Embedded private Mileage mileage;
    @Embedded private Money price;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name="value", column=@Column(name="main_image_url", length=2048)))
    private ImageUrl mainImageUrl;



    protected Vehicle(){}

    private Vehicle(Builder b){
        this.plate = b.plate;
        this.vin = b.vin;
        this.brand = b.brand;
        this.model = b.model;
        this.year = b.year;
        this.mileage = b.mileage;
        this.price = b.price;
        this.mainImageUrl = b.mainImageUrl;
        this.status = VehicleStatus.UNDER_REVIEW;
    }

    public static Builder builder(){ return new Builder(); }
    public static class Builder {
        private Plate plate; private Vin vin;
        private String brand; private String model;
        private YearOfManufacture year; private Mileage mileage; private Money price;
        private ImageUrl mainImageUrl;

        public Builder plate(String v){ this.plate = new Plate(v); return this; }
        public Builder vin(String v){ this.vin = new Vin(v); return this; }
        public Builder brand(String v){ this.brand = v; return this; }
        public Builder model(String v){ this.model = v; return this; }
        public Builder year(int y){ this.year = new YearOfManufacture(y); return this; }
        public Builder mileage(int km){ this.mileage = new Mileage(km); return this; }
        public Builder price(java.math.BigDecimal amount, String cur){ this.price = new Money(amount,cur); return this; }
        public Builder mainImageUrl(String url){ this.mainImageUrl = (url!=null? new ImageUrl(url): null); return this; }

        public Vehicle build(){ return new Vehicle(this); }


    }

    // ===== Domain behavior (reglas de estados del diagrama) =====
    public void markEnRevision(){ this.status = VehicleStatus.UNDER_REVIEW; }
    public void approve(){ this.status = VehicleStatus.AVAILABLE; }
    public void reserve(){ if (status==VehicleStatus.AVAILABLE) this.status = VehicleStatus.RESERVED; }
    public void cancelReservation(){ if (status==VehicleStatus.RESERVED) this.status = VehicleStatus.AVAILABLE; }
    public void markSold(){ if (status==VehicleStatus.AVAILABLE || status==VehicleStatus.RESERVED) this.status = VehicleStatus.SOLD; }
    public void retire(){ this.status = VehicleStatus.WITHDRAWN; }
    public void updateDetails(String brand, String model, int year, int km, java.math.BigDecimal amount, String cur){
        if (brand!=null) this.brand = brand;
        if (model!=null) this.model = model;
        if (year>0) this.year = new YearOfManufacture(year);
        if (km>=0) this.mileage = new Mileage(km);
        if (amount!=null) this.price = new Money(amount, cur!=null?cur:this.price.getCurrency());




    }


    // método de dominio para actualizar solo la imagen principal
    public void changeMainImageUrl(String url){
        this.mainImageUrl = (url!=null? new ImageUrl(url): null);
    }



    /* getters (si usas Lombok puedes @Getter)
    public UUID getId(){ return id; }
    public Plate getPlate(){ return plate; }
    public Vin getVin(){ return vin; }
    public String getBrand(){ return brand; }
    public String getModel(){ return model; }
    public YearOfManufacture getYear(){ return year; }
    public Mileage getMileage(){ return mileage; }
    public Money getPrice(){ return price; }
    public VehicleStatus getStatus(){ return status; }*/
}