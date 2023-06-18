package ch.zhaw.prog2.ticketing.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    void constructorIllegalColumn() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seat('A', -10), "negative column number is not allowed");
        assertThrows(IllegalArgumentException.class,
                () -> new Seat('A', 0), "column 0 is not allowed");
    }

    @Test
    void constructorValidColumn() {
        Seat seat = new Seat('I', 1);
        assertEquals(1, seat.getColumn(), "seat must have set column = 1");
    }
}