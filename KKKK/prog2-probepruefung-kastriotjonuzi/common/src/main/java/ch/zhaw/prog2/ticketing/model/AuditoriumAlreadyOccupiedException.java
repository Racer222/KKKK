package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;

public class AuditoriumAlreadyOccupiedException extends Exception {
    private final Auditorium auditorium;
    private final Show bookedShow;

    public AuditoriumAlreadyOccupiedException(Auditorium auditorium, Show bookedShow) {
        this.auditorium = Objects.requireNonNull(auditorium);
        this.bookedShow = Objects.requireNonNull(bookedShow);
    }

    public AuditoriumAlreadyOccupiedException(String message, Auditorium auditorium, Show bookedShow) {
        super(message);
        this.auditorium = Objects.requireNonNull(auditorium);
        this.bookedShow = Objects.requireNonNull(bookedShow);
    }

    public Show getBookedShow() {
        return bookedShow;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }
}
