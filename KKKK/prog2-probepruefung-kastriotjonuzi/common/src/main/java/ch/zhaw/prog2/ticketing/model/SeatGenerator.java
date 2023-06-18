package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Helper to generate Set of {@link Seat}s
 * <p>
 * To get 5 rows of 10 seats use <code>SeatGenerator.rectangle(5, 10)</code>.
 */
public class SeatGenerator {
    public static final int MAX_ROWS = 26;
    private static final char FIRST_ROW_CHAR = 'A';

    private SeatGenerator() {
        // no instances allowed
    }

    /**
     * Generate {@link Seat}s with given rows and the same number of seats in every row.
     *
     * @param rows        1 <= rows <= {@value MAX_ROWS}
     * @param seatsPerRow must be >= 1
     */
    public static Set<Seat> rectangle(int rows, int seatsPerRow) {
        requireValidRowNumber(rows);
        requireValidSeatNumber(seatsPerRow);
        return byFunction(rows, rowOffset -> seatsPerRow);
    }

    /**
     * Generate given number of rows of {@link Seat}s.
     *
     * <p>The number of seats within one row is calculated using the given function seatsInRow.
     * The parameter of the function is the number of the row, starting with 0.
     * </p>
     * <p>To have 5 seats in the first row, 7 in the second row, 9 in the third row the function can
     * be defined as <code>rowOffset -> 5 + rowOffset * 2</code>.</p>
     *
     * @param rows         1 <= rows <= {@value MAX_ROWS}
     * @param seatsInRow   1 <= seatsInRow
     * @return the set of Seats, never null
     */
    public static Set<Seat> byFunction(int rows, IntUnaryOperator seatsInRow) {
        requireValidRowNumber(rows);
        Objects.requireNonNull(seatsInRow);

        return IntStream.rangeClosed(1, rows)
                .parallel()
                .mapToObj(rowNumber ->
                        generateRowOfSeats(getCharByNumber(rowNumber), seatsInRow.applyAsInt(rowNumber-1)))
                .flatMap(Function.identity()).collect(Collectors.toSet());
    }

    private static void requireValidSeatNumber(int seatsPerRow) {
        if (seatsPerRow < 1) {
            throw new IllegalArgumentException("rows must be >= 1");
        }
    }

    private static void requireValidRowNumber(int rows) {
        requireValidSeatNumber(rows);
        if (rows > MAX_ROWS) {
            throw new IllegalArgumentException(String.format("rows must be <= %d", MAX_ROWS));
        }
    }

    private static Stream<Seat> generateRowOfSeats(char row, int numberOfSeats) {
        return IntStream.rangeClosed(1, numberOfSeats).mapToObj(seatNumber -> new Seat(row, seatNumber));
    }

    /**
     * Converts 1 to 'A', 2 to 'B', ...
     *
     * @param number 1 <= number <= {@value MAX_ROWS}
     */
    private static char getCharByNumber(int number) {
        return (char) (FIRST_ROW_CHAR + (char) number - 1);
    }

    /**
     * Generate Seats in a trapezoid shape
     *
     * <pre>
     * 'A1' .... 'An' with n = seatsInFirstRow
     * .....
     * 'Z1' .... 'Zm' with m = seatsInLastRow
     * </pre>
     *
     * @param rows            1 <= rows <= {@value MAX_ROWS}
     * @param seatsInFirstRow 1 <= seatsInFirstRow
     *                        * @param seatsInLastRow 1 <= seatsInLastRow
     * @return the set of Seats, never null
     */
    public static Set<Seat> trapezoid(int rows, int seatsInFirstRow, int seatsInLastRow) {
        requireValidRowNumber(rows);
        requireValidSeatNumber(seatsInFirstRow);
        requireValidSeatNumber(seatsInLastRow);
        return byFunction(rows, rowOffset -> seatsInFirstRow + rowOffset * (seatsInLastRow - seatsInFirstRow) / (rows - 1));
    }
}
