package ch.zhaw.prog2.ticketing.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import ch.zhaw.prog2.ticketing.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.prog2.ticketing.io.handout.Io;

class IoTest {
    Auditorium auditorium;
    Show show1;
    PriceCalculatorFunction simplePriceCalculator;
    Guest guest1;
    Guest guest2;
    TicketFactory ticketFactoryOfShow1;

    @BeforeEach
    void setup () throws AuditoriumAlreadyOccupiedException, InvalidTicketException {
        simplePriceCalculator = (show, seat) -> 18;
        auditorium = new Auditorium("Riff Raff");
        show1 = new Show(auditorium, "The Big Lebowski", ZonedDateTime.now(), simplePriceCalculator);
        guest1 = new Guest ("Patrick Feisthammel");
        guest2 = new Guest("Michael Wahler");
        ticketFactoryOfShow1 = new TicketFactory(show1);
        Optional<Ticket> ticket1 = ticketFactoryOfShow1.createTicket (new Seat ('D', 1), guest1);
        Optional<Ticket> ticket2 = ticketFactoryOfShow1.createTicket (new Seat ('D', 2), guest2);
        Optional<Ticket> ticket3 = ticketFactoryOfShow1.createTicket (new Seat ('C', 4), guest1);
        Optional<Ticket> ticket4 = ticketFactoryOfShow1.createTicket (new Seat ('D', 4), guest1);
     }

    @Test
    void testExport() throws IOException {
        assertTrue(Io.exportTicketsAsCsv(show1));
    }

}
