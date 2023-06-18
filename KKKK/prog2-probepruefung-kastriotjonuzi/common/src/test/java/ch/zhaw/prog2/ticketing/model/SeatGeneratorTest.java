package ch.zhaw.prog2.ticketing.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class SeatGeneratorTest {

    @Test
    void rectangle_singleSeat() {
        Set<Seat> expected = Set.of(new Seat('A', 1));
        Set<Seat> generated = SeatGenerator.rectangle(1, 1);
        assertIterableEquals(expected, generated, "Expected a singel Seat 'A1'");
    }

    @Test
    void rectangle_oneRow() {
        Set<Seat> expected = Set.of(new Seat('A', 1), new Seat('A', 2), new Seat('A', 3));
        Set<Seat> generated = SeatGenerator.rectangle(1, 3);
        assertIterableEquals(sortedSet(expected), sortedSet(generated), "Expected three Seats 'A1' to 'A3'");
    }

    private List<Seat> sortedSet(Set<Seat> set) {
        return set.stream().sorted().toList();
    }

    @Test
    void rectangle_twoRows() {
        Set<Seat> expected = Set.of(new Seat('A', 1), new Seat('A', 2), new Seat('A', 3), new Seat('B', 1), new Seat('B', 2), new Seat('B', 3));
        Set<Seat> generated = SeatGenerator.rectangle(2, 3);
        assertIterableEquals(sortedSet(expected), sortedSet(generated), "Expected three Seats 'A1' to 'B3'");
    }

    @Test
    void trapezoid_5to2_in3rows() {
        Set<Seat> expected = Set.of(new Seat('A', 1), new Seat('A', 2), new Seat('A', 3), new Seat('A', 4), new Seat('A', 5),
                new Seat('B', 1), new Seat('B', 2), new Seat('B', 3), new Seat('B', 4),
                new Seat('C', 1), new Seat('C', 2));
        Set<Seat> generated = SeatGenerator.trapezoid(3, 5, 2);
        assertIterableEquals(sortedSet(expected), sortedSet(generated), "Expected Seats 'A1', .. 'A5', 'B1', .. 'B4', 'C1', 'C2'");
    }

    @Test
    void trapezoid_2to5_in3rows() {
        Set<Seat> expected = Set.of(new Seat('A', 1), new Seat('A', 2),
                new Seat('B', 1), new Seat('B', 2), new Seat('B', 3),
                new Seat('C', 1), new Seat('C', 2), new Seat('C', 3), new Seat('C', 4), new Seat('C', 5));
        Set<Seat> generated = SeatGenerator.trapezoid(3, 2, 5);
        assertIterableEquals(sortedSet(expected), sortedSet(generated), "Expected Seats 'A1', 'A2', 'B1' .. 'B4', 'C1' .. 'C5'");
    }
}