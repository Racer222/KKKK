package ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking;

import ch.zhaw.prog2.ticketing.model.Guest;
import ch.zhaw.prog2.ticketing.model.Ticket;

import java.util.Collections;
import java.util.List;

/**
 * This class represents the booking and contains the information
 * for the wanted tickets (show and seat) and the person making the booking request
 */
public class Booking {
    /**
     * Possible states of a booking.
     * The CONFIRMED state is used when a booking has been fullfilled
     * => tickets issued
     * The REJECTED state is set, when the booking was canceled
     * => desired seats for the show have already been booked by another request
     * The Booking remains in PENDING state as long as it has not been processed
     * => has so far neither been confirmed nor rejected
     */
    enum ConfirmationState {CONFIRMED, REJECTED, PENDING}

    private final List<AvailableTicket> ticketsToBook;
    private final Guest orderingPerson;
    private ConfirmationState state;
    private List<Ticket> tickets;

    /**
     * Creates a booking for certain show(s) and seat(s)
     *
     * @param ticketsToBook the desired tickets
     * @param orderingPerson the person making the booking request
     */
    public Booking(List<AvailableTicket> ticketsToBook, Guest orderingPerson) {
        this.ticketsToBook = ticketsToBook;
        this.orderingPerson = orderingPerson;
        state = ConfirmationState.PENDING;
    }

    public List<AvailableTicket> getTicketsToBook(){
        return ticketsToBook;
    }

    public Guest getOrderingPerson(){
        return orderingPerson;
    }

    public List<Ticket> getTickets(){
        return Collections.unmodifiableList(tickets);
    }

    /**
     * Checks if the booking has been processed
     * @return false when state is equal to PENDING, true otherwise
     */
    public boolean isProcessed(){
        return !state.equals(ConfirmationState.PENDING);
    }

    /**
     * Checks the confirmation state of the booking
     * @return true when state is equal to CONFIRMED, false otherwise
     */
    public boolean isConfirmed(){
        return state.equals(ConfirmationState.CONFIRMED);
    }

    /**
     * Checks the confirmation state of the booking
     * @return true when state is equal to REJECTED, false otherwise
     */
    public boolean isRejected(){
        return state.equals(ConfirmationState.REJECTED);
    }

    /**
     * Confirms that this booking was successfully processed and
     * the requested tickets in this booking were issued
     *
     * @param tickets the issued tickets for this booking
     */
    public void confirmBooking(List<Ticket> tickets){
        this.tickets = tickets;
        this.state = ConfirmationState.CONFIRMED;
    }

    /**
     * The booking could not be fulfilled
     * because the requested tickets have already been booked by another booking in the meantime
     */
    public void rejectBooking(){
        this.state = ConfirmationState.REJECTED;
    }

}
