package ch.zhaw.prog2.ticketing.concurrency.ticketbooking.handout;

import ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking.AvailableTicket;
import ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking.Booking;
import ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking.TicketBookingService;
import ch.zhaw.prog2.ticketing.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test class tests the implementation of the TicketBookingService in the callable & furtures exercise
 * @see TicketBookingService
 */
public class TicketBookingServiceTest {
    private static final int NUMBER_OF_ROWS_TINY_HALL_AUDITORIUM = 5;
    private static final int NUMBER_OF_SEATS_PER_ROW_TINY_HALL_AUDITORIUM = 3;

    Auditorium tinyHall;
    Show poetrySlam;
    TicketFactory ticketFactoryPoetrySlam;
    List<AvailableTicket> availableTickets;

    TicketBookingService ticketBookingService;

    @BeforeEach
    void setup(){
        tinyHall = new Auditorium("The Tiny Hall", SeatGenerator.rectangle(NUMBER_OF_ROWS_TINY_HALL_AUDITORIUM, NUMBER_OF_SEATS_PER_ROW_TINY_HALL_AUDITORIUM));
        poetrySlam = mock(Show.class);
        when(poetrySlam.getAuditorium()).thenReturn(tinyHall);
        ticketFactoryPoetrySlam = new TicketFactory(poetrySlam);

        availableTickets = generateAvailableTickets(ticketFactoryPoetrySlam);
        ticketBookingService = new TicketBookingService(availableTickets);
    }

    @Test
    void provokeDeadlockSituation() throws InterruptedException, ExecutionException {
        // setup
        Optional<AvailableTicket> a1 = getAvailableTicketForSeat(poetrySlam, 'A', 1);
        Optional<AvailableTicket> a2 = getAvailableTicketForSeat(poetrySlam, 'A', 2);
        Optional<AvailableTicket> b3 = getAvailableTicketForSeat(poetrySlam, 'B', 3);
        // check if seats are existing
        if(!a1.isPresent() || !a2.isPresent() || !b3.isPresent()){
            fail("At least one Seat does not exist, but should.");
        }

        Booking booking1 = new Booking(Arrays.asList(a1.get(), a2.get(), b3.get()), new Guest("Queen Elizabeth"));
        Booking booking2 = new Booking(Arrays.asList(a2.get(), a1.get()), new Guest("Prince Charles"));

        // execute
        ticketBookingService.book(List.of(booking1, booking2));

        // wait a little to give the Threads the chance to finish
        Thread.sleep(2000);

        // Assert that the bookings have been processed after 1 second, if not, there is a deadlock
        assertTrue(booking1.isProcessed(), "Booking 1 was not processed within the given time. There is a deadlock.");
        assertTrue(booking2.isProcessed(), "Booking 2 was not processed within the given time. There is a deadlock.");
    }

    @Test
    void twoNonConflictingBookingsAllTicketsCanBeIssued() throws InterruptedException, ExecutionException {
        final int NUMBER_OF_REQUESTED_TICKETS = 3;
        // setup
        Optional<AvailableTicket> a1 = getAvailableTicketForSeat(poetrySlam, 'A', 1);
        Optional<AvailableTicket> a2 = getAvailableTicketForSeat(poetrySlam, 'A', 2);
        Optional<AvailableTicket> b3 = getAvailableTicketForSeat(poetrySlam, 'B', 3);
        // check if seats are existing
        if(!a1.isPresent() || !a2.isPresent() || !b3.isPresent()){
            fail("At least one Seat does not exist, but should.");
        }

        Booking booking1 = new Booking(Arrays.asList(a1.get(), a2.get()), new Guest("Queen Elizabeth"));
        Booking booking2 = new Booking(Arrays.asList(b3.get()), new Guest("Prince Charles"));

        // execute
        List<Ticket> issuedTickets = ticketBookingService.book(List.of(booking1, booking2));

        // Assert that the bookings have been confirmed
        assertTrue(booking1.isConfirmed(), "Booking 1 was not confirmed but should, because there are no conflicting requests.");
        assertTrue(booking2.isConfirmed(), "Booking 2 was not confirmed but should, because there are no conflicting requests");

        // Assert that tickets from both bookings have been issued
        assertEquals(NUMBER_OF_REQUESTED_TICKETS,issuedTickets.size(), "Booking must return with " + NUMBER_OF_REQUESTED_TICKETS + " issued tickets.");
    }

    /**
     * Helper Method to create available Tickets for a show
     *
     * @param ticketFactory the ticket factory for this show - The ticket factory belongs to exactly one show
     * @return all available tickets fot the show of the given ticket factory
     */
    private List<AvailableTicket> generateAvailableTickets(TicketFactory ticketFactory){
        return ticketFactory.getShow().getAuditorium().getSeats().map(seat -> {
                    return new AvailableTicket(ticketFactory, seat);
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper Method to get the available ticket for show and seat
     *
     * @param show the desired show to get the available ticket for
     * @param row the row of the seat in the auditorium
     * @param column the column of the seat in the auditorium
     * @return an empty optional if there is no available ticket for this seat and show; otherwise, an available ticket wrapped in an Optional
     */
    private Optional<AvailableTicket> getAvailableTicketForSeat(Show show, char row, int column ){
        return availableTickets.stream()
                .filter(t -> t.getShow().equals(show))
                .filter(i -> i.getSeat().equals(new Seat(row,column)))
                .findFirst();
    }
}
