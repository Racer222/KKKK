package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;
import java.util.UUID;

/**
 * The ticket for a given show and a given seat
 */
public class Ticket {
    private final UUID id;
    private final int price;
    private final Seat seat;
    private final Show show;
    private final Guest owner;
    private boolean hasBeenUsed;
    private boolean isPaid;

    public Ticket(Show show, Seat seat, Guest owner) {
        this.id = UUID.randomUUID();
        this.show = Objects.requireNonNull(show);
        this.seat = Objects.requireNonNull(seat);
        this.owner = Objects.requireNonNull(owner);
        this.hasBeenUsed = false;
        this.isPaid = false;
        price = show.getSeatPrice(seat);
    }

    /**
     * Checks if a guest can enter with this ticket.
     *
     * @return true if the ticket has been paid for and has not been used; false otherwise
     */
    public boolean isAllowedToEnter() {
        return isPaid && !hasBeenUsed;
    }

    /**
     * "Uses" the ticket. Does nothing, except for setting hasBeenUsed to true.
     */
    public void useTicket() {
        hasBeenUsed = true;
    }

    /**
     * "Pay" for the ticket. Does nothing, except setting isPaid to true.
     */
    public void pay() {
        this.isPaid = true;
    }

    public Guest getOwner () {
        return owner;
    }

    public Show getShow() {
        return this.show;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public boolean getIsPaid() {
        return this.isPaid;
    }

    public UUID getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }
}
