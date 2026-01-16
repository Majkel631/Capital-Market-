import org.junit.jupiter.api.Test;
import org.zadanko.logic.Order;
import org.zadanko.logic.OrderBook;
import org.zadanko.logic.OrderType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookTest {

    @Test
    void buyOrderWithHigherLimitHasHigherPriority() {
        OrderBook book = new OrderBook();

        Order orderA = new Order("XYZ", 10.0, 100.0, OrderType.BUY);
        Order orderB = new Order("XYZ", 10.0, 105.0, OrderType.BUY);

        book.add(orderA);
        book.add(orderB);

        Order first = book.poll();

        assertEquals(105.0, first.limitPrice, 1e-9);
    }

    @Test
    void sellOrderWithLowerLimitHasHigherPriority() {
        OrderBook book = new OrderBook();

        Order orderA = new Order("XYZ", 10.0, 120.0, OrderType.SELL);
        Order orderB = new Order("XYZ", 10.0, 115.0, OrderType.SELL);

        book.add(orderA);
        book.add(orderB);

        Order first = book.poll();

        assertEquals(115.0, first.limitPrice, 1e-9);
    }
}