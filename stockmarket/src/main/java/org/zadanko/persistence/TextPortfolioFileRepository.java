package org.zadanko.persistence;

import org.zadanko.domain.*;
import org.zadanko.exception.DataIntegrityException;
import org.zadanko.logic.Portfolio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class TextPortfolioFileRepository
        implements PortfolioFileRepository {

    @Override
    public void save(Portfolio portfolio, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {

            writer.write("HEADER|CASH|" + portfolio.getCash());
            writer.newLine();

            for (Asset asset : portfolio.getAssets().values()) {

                double declaredQty = asset.totalQuantity();

                String extra = switch (asset.getAssetType()) {
                    case SHARE -> "-";
                    case COMMODITY ->
                            String.valueOf(
                                    ((Commodity) asset).getStorageRatePerUnit());
                    case CURRENCY ->
                            String.valueOf(
                                    ((Currency) asset).getSpread());
                };

                writer.write("ASSET|"
                        + asset.getAssetType() + "|"
                        + asset.getId() + "|"
                        + declaredQty + "|"
                        + asset.getReferencePrice() + "|"
                        + extra
                );
                writer.newLine();

                for (PurchaseLot lot : asset.getLots()) {
                    writer.write("LOT|"
                            + lot.getDate() + "|"
                            + lot.getQuantity() + "|"
                            + lot.getUnitPrice());
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new DataIntegrityException("I/O error while saving portfolio");
        }
    }

    @Override
    public Portfolio load(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line = reader.readLine();
            if (line == null) {
                throw new DataIntegrityException("Empty file");
            }

            String[] header = line.split("\\|");
            if (header.length != 3 || !header[0].equals("HEADER")) {
                throw new DataIntegrityException("Invalid HEADER line");
            }

            Portfolio portfolio =
                    new Portfolio(Double.parseDouble(header[2]));

            Asset currentAsset = null;
            double declaredQty = 0.0;
            double lotSum = 0.0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("ASSET")) {

                    if (currentAsset != null && lotSum != declaredQty) {
                        throw new DataIntegrityException(
                                "LOT quantity mismatch for asset "
                                        + currentAsset.getId());
                    }

                    AssetType type = AssetType.valueOf(parts[1]);
                    String id = parts[2];
                    declaredQty = Double.parseDouble(parts[3]);
                    double refPrice = Double.parseDouble(parts[4]);
                    String extra = parts[5];

                    lotSum = 0.0;

                    currentAsset = switch (type) {
                        case SHARE ->
                                new Share(id, refPrice);
                        case COMMODITY ->
                                new Commodity(id, refPrice,
                                        Double.parseDouble(extra));
                        case CURRENCY ->
                                new Currency(id, refPrice,
                                        Double.parseDouble(extra));
                    };

                    portfolio.addAsset(currentAsset);

                } else if (parts[0].equals("LOT")) {

                    if (currentAsset == null) {
                        throw new DataIntegrityException("LOT without ASSET");
                    }

                    double qty = Double.parseDouble(parts[2]);
                    double price = Double.parseDouble(parts[3]);

                    lotSum += qty;

                    currentAsset.addLot(
                            new PurchaseLot(
                                    LocalDate.parse(parts[1]),
                                    price,
                                    qty
                            )
                    );

                } else {
                    throw new DataIntegrityException("Unknown line type");
                }
            }

            if (currentAsset != null && lotSum != declaredQty) {
                throw new DataIntegrityException(
                        "LOT quantity mismatch for asset "
                                + currentAsset.getId());
            }

            return portfolio;

        } catch (DataIntegrityException e) {
            throw e;
        } catch (Exception e) {
            throw new DataIntegrityException("Invalid file format");
        }
    }
}
