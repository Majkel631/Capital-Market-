package org.zadanko.domain;

public class Share extends Asset {

    private static final double MAX_FEE = 5.0;
    private static final double FEE_PERCENT = 0.01;

    public Share(String id, double referencePrice) {
        super(id, AssetType.SHARE, referencePrice);
    }

    @Override
    public double realValue(double marketPrice) {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            double gross = marketPrice * lot.getQuantity();
            double fee = Math.min(MAX_FEE, gross * FEE_PERCENT);
            sum += Math.max(0.0, gross - fee);
        }
        return sum;
    }

    @Override
    public double purchaseCost() {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            double gross = lot.getUnitPrice() * lot.getQuantity();
            double fee = Math.min(MAX_FEE, gross * FEE_PERCENT);
            sum += gross + fee;
        }
        return sum;
    }
}
