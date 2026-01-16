package org.zadanko.domain;

public class Currency extends Asset {

    private final double spread;

    public Currency(String id, double referencePrice, double spread) {
        super(id, AssetType.CURRENCY, referencePrice);
        if (spread < 0) {
            throw new IllegalArgumentException("spread must be >= 0");
        }
        this.spread = spread;
    }

    public double getSpread() {
        return spread;
    }

    @Override
    public double realValue(double marketPrice) {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            double bid = marketPrice - spread;
            sum += Math.max(0.0, bid * lot.getQuantity());
        }
        return sum;
    }

    @Override
    public double purchaseCost() {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            sum += lot.getUnitPrice() * lot.getQuantity();
        }
        return sum;
    }
}

