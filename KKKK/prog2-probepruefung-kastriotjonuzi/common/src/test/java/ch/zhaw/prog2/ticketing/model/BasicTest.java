package ch.zhaw.prog2.ticketing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains basic tests for the functionality of the ticketing system.
 */
class BasicTest {
    Auditorium auditorium;
    Show show1;
    Show show2;
    PriceCalculatorFunction simplePriceCalculator;
    Guest guest1;
    Guest guest2;
    TicketFactory ticketFactoryOfShow1;

    @BeforeEach
    void setup () throws AuditoriumAlreadyOccupiedException {
        simplePriceCalculator = (show, seat) -> 18;
        auditorium = new Auditorium("Riff Raff");
        show1 = new Show(auditorium, "The Big Lebowski", ZonedDateTime.now(), simplePriceCalculator);
        show2 = new Show(auditorium,
                "The Big Lebowski",
                ZonedDateTime.of(2022, 10, 29, 22, 0, 0, 0, ZonedDateTime.now().getZone()),
                simplePriceCalculator);
        guest1 = new Guest ("Patrick Feisthammel");
        guest2 = new Guest("Michael Wahler");
        ticketFactoryOfShow1 = new TicketFactory(show1);
    }

    /**
     * Checks that the shows register with their auditorium
     */
    @Test void showRegistration() {
        assertEquals(2, auditorium.getShows().size());
    }

    /**
     * Check if it is possible to create a ticket in the test scenario
     */
    @Test void createTicket() {
        Optional<Ticket> ticket = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticket.isPresent());
    }

    /**
     * Check if it is possible to create two tickets for the same show, but different seats
     */
    @Test void createTwoTicketsSameShow() {
        Optional<Ticket> ticket = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticket.isPresent());
        ticket = ticketFactoryOfShow1.createTicket (new Seat ('A', 2), guest2);
        assertTrue (ticket.isPresent());
    }

    /**
     * Check if it is possible to create two tickets for the same seat, but different shows
     */
    @Test void createTwoTicketsSameSeat() {
        Optional<Ticket> ticket = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticket.isPresent());
        ticket = new TicketFactory(show2).createTicket (new Seat ('A', 1), guest2);
        assertTrue (ticket.isPresent());
    }


    /**
     * Check that ticket is valid only after it has been paid for
     */
    @Test void payTicket() {
        Optional<Ticket> ticketPackage = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticketPackage.isPresent());
        Ticket ticket = ticketPackage.get();
        assertFalse (ticket.isAllowedToEnter());
        ticket.pay ();
        assertTrue (ticket.isAllowedToEnter());
    }

    /**
     * Check that it is not possible to buy two tickets for the same seat and show.
     */
    @Test void createDoubleTicket() {
        Optional<Ticket> ticket1 = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticket1.isPresent());
        Optional<Ticket> ticket2 = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest2);
        assertTrue (ticket2.isEmpty());
    }

    /**
     * Check that un-booking a ticket works
     */
    @Test void cancelTicket() {
        Optional<Ticket> ticketPackage = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticketPackage.isPresent());
        Ticket ticket = ticketPackage.get();
        assertTrue (auditorium.isTicketRegistered(ticket));

        auditorium.cancelTicket(ticket);
        assertFalse (auditorium.isTicketRegistered(ticket));
    }

    /**
     * Check that a paid ticket for show1 can be used exactly once for show1, but never for show2
     */
    @Test void useTicket() {
        Optional<Ticket> ticketPackage = ticketFactoryOfShow1.createTicket (new Seat ('A', 1), guest1);
        assertTrue (ticketPackage.isPresent());
        Ticket ticket = ticketPackage.get();
        ticket.pay();

        Optional<Entrance> someEntrance = auditorium.getEntrances().stream().findAny();
        assertTrue (someEntrance.isPresent());

        Entrance entrance = someEntrance.get();
        // ticket is not valid for show 2
        assertFalse(entrance.checkTicket(show2, ticket));
        // ticket is valid for show 1
        assertTrue(entrance.checkTicket(show1, ticket));
        // ticket is not valid ANYMORE for show 1
        assertFalse(entrance.checkTicket(show1, ticket));
    }

}
