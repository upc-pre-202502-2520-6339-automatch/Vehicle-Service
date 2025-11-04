package com.vehicle.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class Money {
    private BigDecimal amount;
    private String currency; // "PEN", "USD"

    protected Money() {}
    public Money(BigDecimal amount, String currency) {
        if (amount == null || amount.signum() < 0) throw new IllegalArgumentException("amount >= 0");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("currency ISO3");
        this.amount = amount;
        this.currency = currency.toUpperCase();
    }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    @Override public boolean equals(Object o){
        return o instanceof Money m && amount.compareTo(m.amount)==0 && Objects.equals(currency,m.currency);
    }
    @Override public int hashCode(){ return Objects.hash(amount, currency); }
}