package ch.zhaw.prog2.ticketing.model;

import java.util.Objects;

public class Entrance {

    /**
     * Checks a ticket and makes it invalid
     *
     * @param show   The show to attend.
     * @param ticket A ticket that must be for the show.
     * @return true if the ticket was valid, false otherwise
     */
    public boolean checkTicket(Show show, Ticket ticket) {
        Objects.requireNonNull(show);
        Objects.requireNonNull(ticket);
        boolean success = false;

        if (ticket.getShow().equals(show) && ticket.isAllowedToEnter()) {
            ticket.useTicket();
            success = true;
        } else {
            success = false;
        }

        return success;
    }
}
