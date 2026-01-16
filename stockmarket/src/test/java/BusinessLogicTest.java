import org.junit.jupiter.api.Test;
import org.zadanko.domain.Commodity;
import org.zadanko.domain.Currency;
import org.zadanko.domain.PurchaseLot;
import org.zadanko.domain.Share;
import org.zadanko.exception.InsufficientFundsException;
import org.zadanko.logic.Portfolio;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessLogicTest {

    @Test
    void polymorphismDifferentRealValuesBasedOnLots() {
        Share share = new Share("S1", 100.0);
        Commodity commodity = new Commodity("C1", 100.0, 2.0);
        Currency currency = new Currency("CU1", 100.0, 1.5);

        share.addLot(new PurchaseLot(LocalDate.now(), 100.0, 10.0));
        commodity.addLot(new PurchaseLot(LocalDate.now(), 100.0, 10.0));
        currency.addLot(new PurchaseLot(LocalDate.now(), 100.0, 10.0));

        double marketPrice = 100.0;

        double vShare = share.realValue(marketPrice);
        double vCommodity = commodity.realValue(marketPrice);
        double vCurrency = currency.realValue(marketPrice);

        assertNotEquals(vShare, vCommodity);
        assertNotEquals(vShare, vCurrency);
        assertNotEquals(vCommodity, vCurrency);
    }

    @Test
    void assetRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new Share(null, 100.0));

        assertThrows(IllegalArgumentException.class,
                () -> new Share("S", -10.0));
    }

    @Test
    void purchaseFailsWhenInsufficientFunds() {
        Portfolio portfolio = new Portfolio(50.0);

        Share share = new Share("S-exp", 100.0);
        share.addLot(new PurchaseLot(LocalDate.now(), 100.0, 1.0));

        InsufficientFundsException ex = assertThrows(
                InsufficientFundsException.class,
                () -> portfolio.buy(share)
        );

        assertTrue(ex.getMessage().contains("Not enough cash"));
    }

    @Test
    void portfolioAuditSumsPolymorphicValues() throws InsufficientFundsException {
        Portfolio p = new Portfolio(5000.0);

        Share share = new Share("S2", 100.0);
        share.addLot(new PurchaseLot(LocalDate.now(), 100.0, 5.0));

        Commodity commodity = new Commodity("C2", 50.0, 1.0);
        commodity.addLot(new PurchaseLot(LocalDate.now(), 50.0, 10.0));

        Currency currency = new Currency("CU2", 10.0, 0.5);
        currency.addLot(new PurchaseLot(LocalDate.now(), 10.0, 10.0));

        p.buy(share);
        p.buy(commodity);
        p.buy(currency);

        double audit = p.auditTotalValue();
        double expected = share.realValue(100.0)
                + commodity.realValue(50.0)
                + currency.realValue(10.0);

        assertEquals(expected, audit, 1e-9);
    }
}

