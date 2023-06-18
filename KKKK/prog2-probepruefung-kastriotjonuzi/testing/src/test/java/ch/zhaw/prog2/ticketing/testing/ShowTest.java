package ch.zhaw.prog2.ticketing.testing;

import ch.zhaw.prog2.ticketing.model.*;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ShowTest {

    private final ZonedDateTime now = ZonedDateTime.now();

    // Stub für die Preis-Berechnung
    PriceCalculatorFunction priceStub = (show, seat) -> 15;

    @Test
    public void testShow() throws AuditoriumAlreadyOccupiedException  {



        // Initialisieren Sie den Mock [0.5 P]


        // Initialisieren Sie die Class Under Test [0.5 P]


        // Verifizieren Sie, dass das Auditorium für die Show wirklich gebucht wurde:  [1 P]


        // Verifizieren Sie, dass der Seat-Preis wirklich mit unserem Preis-Stub berechnet wird (=15):  [1 P]


        // Verifizieren Sie, dass die Location der Show wirklich dem Namen des Auditoriums entspricht und dort abgefragt wurde:  [2 P]



    }

}
