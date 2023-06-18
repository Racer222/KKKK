package ch.zhaw.prog2.ticketing.model;

/**
 * Functional interface for a function that computes the price
 * of a ticket based on a show and a seat.
 */
@FunctionalInterface
public interface PriceCalculatorFunction {
    int calculatePriceInRappen(Show show, Seat seat);
}
