package org.zadanko.logic;

public class Order {
    public final String assetId;
    public final double quantity;
    public final double limitPrice;
    public final OrderType type;

    public Order(String assetId, double quantity,
                 double limitPrice, OrderType type) {
        this.assetId = assetId;
        this.quantity = quantity;
        this.limitPrice = limitPrice;
        this.type = type;
    }
}