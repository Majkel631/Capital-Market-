package org.zadanko.domain;

import org.zadanko.exception.InsufficientAssetsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Asset {

    protected final String id;
    protected final AssetType assetType;
    protected final double referencePrice;
    protected final List<PurchaseLot> lots = new ArrayList<>();

    protected Asset(String id, AssetType assetType, double referencePrice) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (assetType == null) {
            throw new IllegalArgumentException("assetType must not be null");
        }
        if (referencePrice <= 0) {
            throw new IllegalArgumentException("referencePrice must be > 0");
        }
        this.id = id;
        this.assetType = assetType;
        this.referencePrice = referencePrice;
    }

    public String getId() {
        return id;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public double getReferencePrice() {
        return referencePrice;
    }

    public List<PurchaseLot> getLots() {
        return Collections.unmodifiableList(lots);
    }

    public void addLot(PurchaseLot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("lot must not be null");
        }
        lots.add(lot);
    }

    public double totalQuantity() {
        double sum = 0.0;
        for (PurchaseLot lot : lots) {
            sum += lot.getQuantity();
        }
        return sum;
    }

    public double realValue() {
        return realValue(referencePrice);
    }

    public abstract double realValue(double marketPrice);

    public abstract double purchaseCost();


    public double sellFifo(double quantity, double sellPrice)
            throws InsufficientAssetsException {

        double remaining = quantity;

        for (PurchaseLot lot : lots) {
            if (remaining <= 0) break;
            remaining -= Math.min(remaining, lot.getQuantity());
        }

        if (remaining > 0) {
            throw new InsufficientAssetsException("Not enough assets");
        }

        remaining = quantity;
        double profit = 0.0;
        int i = 0;

        while (i < lots.size() && remaining > 0) {
            PurchaseLot lot = lots.get(i);
            double used = Math.min(remaining, lot.getQuantity());

            profit += used * (sellPrice - lot.getUnitPrice());
            lot.reduceQuantity(used);
            remaining -= used;

            if (lot.isEmpty()) {
                lots.remove(i);
            } else {
                i++;
            }
        }

        return profit;
    }
}
