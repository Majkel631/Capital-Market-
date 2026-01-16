package org.zadanko.domain;

public class Commodity extends Asset {

    private final double storageRatePerUnit;

    public Commodity(String id, double referencePrice, double storageRatePerUnit) {
        super(id, AssetType.COMMODITY, referencePrice);
        if (storageRatePerUnit < 0) {
            throw new IllegalArgumentException("storageRatePerUnit must be >= 0");
        }
        this.storageRatePerUnit = storageRatePerUnit;
    }

    public double getStorageRatePerUnit() {
        return storageRatePerUnit;
    }

    @Override
    public double realValue(double marketPrice) {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            double gross = marketPrice * lot.getQuantity();
            double storage = storageRatePerUnit * lot.getQuantity();
            sum += Math.max(0.0, gross - storage);
        }
        return sum;
    }

    @Override
    public double purchaseCost() {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            double gross = lot.getUnitPrice() * lot.getQuantity();
            double storage = storageRatePerUnit * lot.getQuantity();
            sum += gross + storage;
        }
        return sum;
    }
}

