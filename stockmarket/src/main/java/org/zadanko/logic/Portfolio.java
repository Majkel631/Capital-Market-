package org.zadanko.logic;

import org.zadanko.domain.*;
import org.zadanko.exception.InsufficientFundsException;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Portfolio {

    private double cash;
    private final Map<String, Asset> assets = new HashMap<>();
    private final Set<String> watchlist = new HashSet<>();

    public Portfolio(double initialCash) {
        if (initialCash < 0) {
            throw new IllegalArgumentException("cash must be >= 0");
        }
        this.cash = initialCash;
    }

    public double getCash() {
        return cash;
    }

    public void addCash(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        cash += amount;
    }

    public void removeCash(double amount) throws InsufficientFundsException {
        if (amount > cash) {
            throw new InsufficientFundsException("Not enough cash");
        }
        cash -= amount;
    }

    public Asset getAsset(String id) {
        return assets.get(id);
    }

    public Map<String, Asset> getAssets() {
        return assets;
    }

    public void addToWatchlist(String id) {
        watchlist.add(id);
    }


    public void buy(Asset asset) throws InsufficientFundsException {
        if (asset == null) {
            throw new IllegalArgumentException("asset must not be null");
        }

        double cost = asset.purchaseCost();
        if (cost > cash) {
            throw new InsufficientFundsException("Not enough cash");
        }

        cash -= cost;
        assets.put(asset.getId(), asset);
    }

    public void addAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("asset must not be null");
        }
        assets.put(asset.getId(), asset);
    }

    public void buy(String assetId,
                    AssetType type,
                    double unitPrice,
                    double quantity,
                    double referencePrice,
                    double extra,
                    LocalDate date)
            throws InsufficientFundsException {

        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("assetId invalid");
        }
        if (quantity <= 0 || unitPrice <= 0) {
            throw new IllegalArgumentException("Invalid buy parameters");
        }

        double cost = unitPrice * quantity;
        if (cost > cash) {
            throw new InsufficientFundsException("Not enough cash");
        }

        Asset asset = assets.get(assetId);

        if (asset == null) {
            switch (type) {
                case SHARE:
                    asset = new Share(assetId, referencePrice);
                    break;
                case COMMODITY:
                    asset = new Commodity(assetId, referencePrice, extra);
                    break;
                case CURRENCY:
                    asset = new Currency(assetId, referencePrice, extra);
                    break;
                default:
                    throw new IllegalStateException("Unknown asset type");
            }
            assets.put(assetId, asset);
        }

        asset.addLot(new PurchaseLot(date, unitPrice, quantity));
        cash -= cost;
    }

    public double auditTotalValue() {
        double sum = 0.0;
        for (Asset asset : assets.values()) {
            sum += asset.realValue();
        }
        return sum;
    }
}

