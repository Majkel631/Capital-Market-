package org.zadanko.report;

import org.zadanko.domain.Asset;
import org.zadanko.logic.Portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioReportGenerator {

    public String generate(Portfolio portfolio, Map<String, Double> marketPrices) {

        List<Asset> assets = new ArrayList<>(portfolio.getAssets().values());
        assets.sort(new AssetComparator(marketPrices));

        StringBuilder sb = new StringBuilder();
        sb.append("PORTFOLIO REPORT\n");

        for (Asset asset : assets) {
            double value = asset.realValue(marketPrices.get(asset.getId()));
            sb.append(asset.getId())
                    .append(" | ")
                    .append(value)
                    .append("\n");
        }

        return sb.toString();
    }
}