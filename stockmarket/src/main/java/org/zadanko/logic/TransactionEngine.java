package org.zadanko.logic;

import org.zadanko.domain.Asset;
import org.zadanko.exception.InsufficientAssetsException;



public class TransactionEngine {

    public double sell(Portfolio portfolio,
                       String assetId,
                       double quantity,
                       double sellPrice)
            throws InsufficientAssetsException {

        Asset asset = portfolio.getAsset(assetId);
        if (asset == null) {
            throw new InsufficientAssetsException("Asset not found");
        }

        double profit = asset.sellFifo(quantity, sellPrice);
        portfolio.addCash(quantity * sellPrice);

        return profit;
    }
}
