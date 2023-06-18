package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;

/**
 * Represents a seat in an auditorium
 */
public class Seat implements Comparable<Seat> {
    private final char row;
    private final int column;

    /**
     * Creates a new {@link Seat} which is identified by the two given parameters.
     *
     * @param row any Character
     * @param column 1 <= column <= INTEGER.MAX_VALUE or an IllegalArgumentException is thrown
     */
    public Seat(char row, int column) {
        if (column < 1) {
            throw new IllegalArgumentException("column must be >= 1");
        }
        this.row = row;
        this.column = column;
    }

    public char getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && column == seat.column;
    }

    @Override
    public String toString() {
        return String.format("%s%d", row, column);
    }

    @Override
    public int compareTo(Seat o) {
        Objects.requireNonNull(o, "never compare to null");
        return row == o.row ? Integer.compare(column, o.column) : Character.compare(row, o.row);
    }
}
