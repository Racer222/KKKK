package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;
import java.util.Optional;

/**
 * This class creates tickets and registers them with the auditorium
 */
public class TicketFactory {
    private final Show show;

    public TicketFactory(Show show) {
        this.show = Objects.requireNonNull(show);
    }

    /**
     * Creates a ticket and registers it with the auditorium of the show
     *
     * @param seat  the desired seat
     * @param owner the owner of the ticket
     * @return an empty optional if the ticket could not be registered; otherwise, a ticket wrapped in an Optional
     */
    public Optional<Ticket> createTicket(Seat seat, Guest owner) {
        Objects.requireNonNull(seat);
        Objects.requireNonNull(owner);
        Ticket ticketCandidate = new Ticket(show, seat, owner);
        try {
            show.getAuditorium().bookTicket(ticketCandidate);
        } catch (InvalidTicketException exception) {
            return Optional.empty();
        }

        return Optional.of(ticketCandidate);
    }

    public Show getShow() {
        return show;
    }
}
