package ch.zhaw.prog2.ticketing.model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * This class represents one show of an auditorium at a given time.
 * Shows are distinguished by their names and their dates.
 * Each show is given a price calculator function that calculates ticket prices.
 */
public class Show {
    private final Auditorium auditorium;
    private final String name;
    private final PriceCalculatorFunction priceCalculatorFunction;
    private final ZonedDateTime dateTime;
    public Show(Auditorium auditorium, String name, ZonedDateTime showTime, PriceCalculatorFunction priceCalculatorFunction) throws AuditoriumAlreadyOccupiedException {
        this.auditorium = Objects.requireNonNull(auditorium);
        this.name = Objects.requireNonNull(name);
        this.dateTime = Objects.requireNonNull(showTime);
        this.priceCalculatorFunction = Objects.requireNonNull(priceCalculatorFunction);
        auditorium.bookAuditoriumFor(this);
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public int getSeatPrice(Seat seat) {
        Objects.requireNonNull(seat);
        return priceCalculatorFunction.calculatePriceInRappen(this, seat);
    }

    public PriceCalculatorFunction getPriceCalculatorFunction() {
        return priceCalculatorFunction;
    }

    public String getName() {
        return name;
    }

    public Auditorium getAuditorium() {
        return this.auditorium;
    }

    /**
     * Gets the Name of the Auditorium where the Show will happen.
     * @return the location
     */
    public String getLocation(){
        return auditorium.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Objects.equals(name, show.name) && Objects.equals(dateTime, show.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateTime);
    }
}
