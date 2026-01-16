package org.zadanko.domain;

import java.time.LocalDate;

public class PurchaseLot {

    private static final double EPS = 1e-9;

    private final LocalDate date;
    private final double unitPrice;
    private double quantity;

    public PurchaseLot(LocalDate date, double unitPrice, double quantity) {
        if (date == null || unitPrice <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid lot data");
        }
        this.date = date;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void reduceQuantity(double amount) {
        if (amount <= 0 || amount > quantity + EPS) {
            throw new IllegalArgumentException("Invalid reduce amount");
        }
        quantity -= amount;
        if (quantity < EPS) {
            quantity = 0.0;
        }
    }

    public boolean isEmpty() {
        return quantity <= EPS;
    }
}
