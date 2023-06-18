package ch.zhaw.prog2.ticketing.functional.handout;

import ch.zhaw.prog2.ticketing.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FunctionalTest {

    FunctionalAuditorium auditorium;
    Show show1;
    Show show2;
    PriceCalculatorFunction simplePriceCalculator;
    Guest guest1;
    Guest guest2;
    Ticket ticket1;
    Ticket ticket2;
    TicketFactory ticketFactoryOfShow1;

    private final int TICKET_PRICE = 1800;

    @BeforeEach
    void setup () throws AuditoriumAlreadyOccupiedException {
        simplePriceCalculator = (show, seat) -> TICKET_PRICE;
        auditorium = new FunctionalAuditorium("Riff Raff", SeatGenerator.rectangle(26,50));
        show1 = new Show(auditorium, "The Big Lebowski", ZonedDateTime.now(), simplePriceCalculator);
        show2 = new Show(auditorium,
                "The Big Lebowski",
                ZonedDateTime.of(2022, 9, 29, 22, 0, 0, 0, ZonedDateTime.now().getZone()),
                simplePriceCalculator);
        guest1 = new Guest ("Patrick Feisthammel");
        guest2 = new Guest("Michael Wahler");
        ticketFactoryOfShow1 = new TicketFactory(show1);
        ticket1 = ticketFactoryOfShow1.createTicket (new Seat('A', 1), guest1).get();
        ticket2 = ticketFactoryOfShow1.createTicket (new Seat ('A', 2), guest2).get();
    }

    @Disabled
    @Test
    void testSeatsInRegisteredTickets () {
        assertEquals(2, auditorium.seatsInRegisteredTickets(show1).size());
        assertEquals(0, auditorium.seatsInRegisteredTickets(show2).size());
    }

    @Disabled
    @Test
    void testTicketsOfAllGuests () {
        Map<Guest, List<Ticket>> guestTickets = auditorium.ticketsOfAllGuests();

        assertTrue (guestTickets.get(guest1).contains(ticket1));
        assertTrue (guestTickets.get(guest2).contains(ticket2));
        assertEquals(1, guestTickets.get(guest1).size());
        assertEquals(1, guestTickets.get(guest2).size());
    }

    @Disabled
    @Test
    void testGetNumberOfNoShows () {
        // ticket not yet paid
        assertEquals (0, auditorium.getNumberOfNoShows(show1));

        // ticket paid, but not used
        ticket1.pay();
        assertEquals (1, auditorium.getNumberOfNoShows(show1));

        // ticket used
        ticket1.useTicket();
        assertEquals (0, auditorium.getNumberOfNoShows(show1));
    }

    @Disabled
    @Test
    void testGetRevenueOfShow() {
        assertEquals(2*TICKET_PRICE, auditorium.getRevenueOfShow(show1));
        assertEquals(0, auditorium.getRevenueOfShow(show2));
    }

    @Test
    void testUniqueGuestsPerShow () {
        List<String> expectedGuests = Stream.of(guest1.getName(), guest2.getName()).sorted().toList();
        List<String> guests = auditorium.getUniqueGuestsForShow(show1);
        Assertions.assertEquals(expectedGuests, guests);
    }

    @Test
    void topUsedSeats() throws AuditoriumAlreadyOccupiedException, InvalidTicketException {
        auditorium = new FunctionalAuditorium("Empty Auditorium", SeatGenerator.rectangle(26,50));
        Set<Seat> seats = IntStream.rangeClosed(1, 10).mapToObj(i -> new Seat('R', i)).collect(Collectors.toSet());

        // create one ticket for seats R5 to R10
        registerTicketInNewShow(seats.stream().sorted().skip(4));

        // create an additional ticket for seats R5 to R7
        registerTicketInNewShow(seats.stream().sorted().skip(4).limit(3));

        // crate an additional ticket for seat R6
        registerTicketInNewShow(seats.stream().sorted().skip(5).limit(1));

        assertEquals(3, auditorium.getShows().size(), "we registered tickets in three shows");

        List<Seat> topFound = auditorium.topUsedSeats(3);
        List<Seat> expected = List.of(new Seat('R', 6), new Seat('R', 5), new Seat('R', 7));
        assertIterableEquals(expected, topFound);
    }

    @Test
    void testDiscountBefore5PM() throws AuditoriumAlreadyOccupiedException {
        final int REGULAR_TICKET_PRICE = 1800;
        final double DISCOUNT = 0.8;
        final int REDUCED_TICKET_PRICE = 1440;

        Seat seat = new Seat ('M', 42);
        PriceCalculatorFunction earlyTimeDiscount = PriceCalculators.getDiscountBefore5PM(REGULAR_TICKET_PRICE, DISCOUNT);
        Show lateShow = new Show(auditorium,
                "The Life Aquatic with Steve Zissou",
                ZonedDateTime.of(2022, 10, 29, 22, 0, 0, 0, ZonedDateTime.now().getZone()),
                earlyTimeDiscount);

        Show earlyShow = new Show(auditorium,
                "The Life Aquatic with Steve Zissou",
                ZonedDateTime.of(2022, 10, 29, 16, 59, 0, 0, ZonedDateTime.now().getZone()),
                earlyTimeDiscount);

        assertEquals(REGULAR_TICKET_PRICE, lateShow.getSeatPrice(seat));
        assertEquals(REDUCED_TICKET_PRICE, earlyShow.getSeatPrice(seat));
    }

    private void registerTicketInNewShow(Stream<Seat> seats) throws AuditoriumAlreadyOccupiedException {
        Show show = mock(Show.class);
        when(show.getAuditorium()).thenReturn(auditorium);
        when(show.getDateTime()).thenReturn(ZonedDateTime.now());
        auditorium.bookAuditoriumFor(show);
        TicketFactory ticketFactory = new TicketFactory(show);
        seats.forEach(seat -> ticketFactory.createTicket(seat, mock(Guest.class)));
    }

}
