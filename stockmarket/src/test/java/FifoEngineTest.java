import org.junit.jupiter.api.Test;
import org.zadanko.domain.PurchaseLot;
import org.zadanko.domain.Share;
import org.zadanko.exception.InsufficientAssetsException;
import org.zadanko.logic.Portfolio;
import org.zadanko.logic.TransactionEngine;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FifoEngineTest {

    @Test
    void fifoSellConsumesOldestLotsFirst() throws InsufficientAssetsException {

        Share share = new Share("XYZ", 1.0);

        share.addLot(new PurchaseLot(
                LocalDate.of(2023, 1, 1), 100.0, 10.0
        ));
        share.addLot(new PurchaseLot(
                LocalDate.of(2023, 2, 1), 120.0, 10.0
        ));

        Portfolio portfolio = new Portfolio(0.0);
        portfolio.addAsset(share);

        TransactionEngine engine = new TransactionEngine();


        double profit = engine.sell(portfolio, "XYZ", 15.0, 150.0);


        assertEquals(650.0, profit, 1e-9);
        assertEquals(5.0, share.getLots().get(0).getQuantity(), 1e-9);
        assertEquals(1, share.getLots().size());
    }

    @Test
    void sellingMoreThanOwnedThrowsException() {
        Share share = new Share("ABC", 1.0);

        share.addLot(new PurchaseLot(
                LocalDate.of(2023, 1, 1), 100.0, 5.0
        ));

        Portfolio portfolio = new Portfolio(0.0);
        portfolio.addAsset(share);

        TransactionEngine engine = new TransactionEngine();

        assertThrows(
                InsufficientAssetsException.class,
                () -> engine.sell(portfolio, "ABC", 10.0, 150.0)
        );
    }
}
