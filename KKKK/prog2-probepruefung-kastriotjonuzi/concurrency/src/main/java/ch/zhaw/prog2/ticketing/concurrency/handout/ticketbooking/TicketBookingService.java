package ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking;
import ch.zhaw.prog2.ticketing.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * This class represents the booking system for the available tickets.
 * Multiple bookings can be processed concurrent with the method {@link #book(List) book}
 */
public class TicketBookingService {
    private List<AvailableTicket> availableTickets;

    /**
     * Creates a new ticket booking service instance
     *
     * @param availableTickets the current available tickets for all shows
     */
    public TicketBookingService(List<AvailableTicket> availableTickets){
        this.availableTickets = availableTickets;
    }

    /**
     * Concurrent processing of a collection of bookings
     *
     * @param bookings the bookings to be fulfilled
     * @return all issued tickets during this processing
     */
    public List<Ticket> book(List<Booking> bookings) throws InterruptedException, ExecutionException {
        List<Ticket> issuedTickets = new ArrayList<>();

        for(Booking booking : bookings) {
            new Thread(new BookingRequest(booking)).start();
        }

        return issuedTickets;
    }

    private class BookingRequest implements Runnable {
        private final Booking booking;
        List<Ticket> issuedTickets = new ArrayList<>();

        /**
         * Creates a booking request for a certain show and seat(s)
         *
         * @param booking the desired booking
         */
        public BookingRequest(Booking booking) {
            this.booking = booking;
        }

        @Override
        public void run() {
            boolean bookingRequestFullfilled = true;
            List<AvailableTicket> ticketsToBook = booking.getTicketsToBook();
            try {
                // lock available tickets of the booking to ensure mutual exclusive access
                for (AvailableTicket availableTicket : ticketsToBook) {
                    availableTicket.lock();
                }
                // try to execute the booking and issue the ticket, if ticket can not be issued cancel booking
                for (AvailableTicket availableTicket : ticketsToBook) {
                    Optional<Ticket> ticket = availableTicket.issueTicket(booking.getOrderingPerson());
                    if (ticket.isPresent()) {
                        issuedTickets.add(ticket.get());
                    } else {
                        bookingRequestFullfilled = false;
                        break;
                    }
                }
            } finally {
                // if booking is successfully fulfilled confirm booking request, otherwise reject.
                if (bookingRequestFullfilled) {
                    confirm();
                } else {
                    reject();
                }
                // unlock all resources
                for (AvailableTicket availableTicket : ticketsToBook) {
                    availableTicket.unlock();
                }
            }
        }

        /**
         * If booking is successfully fulfilled
         *     - confirm the booking
         *     - remove tickets from list of available ticket
         *     - print result to console
         */
        private void confirm() {
            booking.confirmBooking(issuedTickets);
            availableTickets.removeAll(booking.getTicketsToBook());
            System.out.println("Successfully booked " + issuedTickets);
        }

        /**
         * If booking can not be fulfilled
         *     - reject the booking
         *     - cancel already issued tickets
         *     - clear list of issuedTickets (empty list)
         *     - print result to console
         */
        private void reject() {
            booking.rejectBooking();
            for (Ticket ticket : issuedTickets) {
                ticket.getShow().getAuditorium().cancelTicket(ticket);
            }
            issuedTickets.clear();
            System.out.println("Failed to book " + booking.toString());
        }
    }
}