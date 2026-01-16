package org.zadanko.logic;

import java.util.PriorityQueue;

public class OrderBook {

    private final PriorityQueue<Order> queue =
            new PriorityQueue<>(new OrderComparator());

    public void add(Order order) {
        queue.add(order);
    }

    public Order poll() {
        return queue.poll();
    }
}