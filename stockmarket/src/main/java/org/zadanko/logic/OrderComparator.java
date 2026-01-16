package org.zadanko.logic;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        if (o1.type != o2.type) {
            return o1.type.compareTo(o2.type);
        }
        if (o1.type == OrderType.BUY) {
            return Double.compare(o2.limitPrice, o1.limitPrice);
        }
        return Double.compare(o1.limitPrice, o2.limitPrice);
    }
}