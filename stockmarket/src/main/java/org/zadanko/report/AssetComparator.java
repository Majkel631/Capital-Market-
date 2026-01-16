package org.zadanko.report;

import org.zadanko.domain.Asset;

import java.util.Comparator;
import java.util.Map;

public class AssetComparator implements Comparator<Asset> {

    private final Map<String, Double> marketPrices;

    public AssetComparator(Map<String, Double> marketPrices) {
        this.marketPrices = marketPrices;
    }

    @Override
    public int compare(Asset a1, Asset a2) {

        int typeCompare =
                a1.getAssetType().compareTo(a2.getAssetType());
        if (typeCompare != 0) {
            return typeCompare;
        }

        double v1 = a1.realValue(marketPrices.get(a1.getId()));
        double v2 = a2.realValue(marketPrices.get(a2.getId()));

        return Double.compare(v2, v1);
    }
}