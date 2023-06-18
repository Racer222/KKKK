package ch.zhaw.prog2.ticketing.functional.handout;

import ch.zhaw.prog2.ticketing.model.Auditorium;
import ch.zhaw.prog2.ticketing.model.Guest;
import ch.zhaw.prog2.ticketing.model.Seat;
import ch.zhaw.prog2.ticketing.model.Show;
import ch.zhaw.prog2.ticketing.model.Ticket;

import java.util.*;
import java.util.stream.Collectors;

public class FunctionalAuditorium extends Auditorium {
    public FunctionalAuditorium (String name, Set<Seat> seats) {
        super (name, seats);
    }

    /**
     * Erstellt eine Menge aller Sitze ({@link Seat}) für die gewünschte Show.
     * <p>
     * Hinweis: Benutzen Sie die Methode getRegisteredTicketsFor(Show) der Klasse
     * {@link Auditorium}.
     *
     * @param show darf nicht null sein
     * @return niemals null
     */
    public Set<Seat> seatsInRegisteredTickets(Show show) {
        Objects.requireNonNull(show);
        return null; // TODO implementieren
    }

    /**
     * Erstellt eine Map. Der Key ist ein {@link Guest}, der Wert ist eine Liste
     * aller {@link Ticket Tickets}, die der Guest besitzt.
     * <p>
     * Hinweis: Benutzen Sie die Methode getRegisteredTicketsFor(Show) der Klasse
     * {@link Auditorium}.
     *
     * @return alle Tickets pro Guest
     */
    public Map<Guest, List<Ticket>> ticketsOfAllGuests() {
        return null; // TODO implementieren
    }

    /**
     * Berechnet die Anzahl der registrierten {@link Ticket Tickets}, die zum Eintritt berechtigen.
     * <p>
     * Hinweis: Die gesuchten Tickets liefern den Wert <code>true</code> als Rückgabewert der Methode
     * {@link Ticket#isAllowedToEnter()}.
     *
     * @param show darf nicht null sein
     * @return eine Zahl >= 0
     */
    public long getNumberOfNoShows (Show show) {
        Objects.requireNonNull(show);
        return 0; // TODO implementieren
    }

    /**
     * Berechnet den Gesamtumsatz für die gegebene {@link Show}.
     *
     * @param show darf nicht null sein
     * @return eine Zahl >= 0
     */
    public int getRevenueOfShow(Show show) {
        Objects.requireNonNull(show);
        return 0; // TODO implementieren
    }

    /**
     * Erstellt eine alphabetisch sortierte Liste von Guest-Namen für die gewünschte Show.
     * Kein Guest-Name darf mehr als einmal in der Liste erscheinen.
     * <p>
     * Hinweis: Die Methode sorted() der Klasse Stream sortiert einen Stream<String> alphabetisch.<br />
     * Hinweis: Benutzen Sie die Methode {@link Auditorium#getRegisteredTicketsFor(Show)}.
     *
     * @param show darf nicht null sein
     * @return Eine Liste von alphabetisch sortierten Strings ohne doppelte Einträge
     */
    public List<String> getUniqueGuestsForShow (Show show) {
        Objects.requireNonNull(show);
        return null; // TODO implementieren
    }

    /**
     * Berechnet die n am häufigsten gebuchten {@link Seat Seats} aller registrierten {@link Ticket Tickets}.
     * <p>
     * Hinweis: Benutzen Sie die Klasse {@link ObjectCounter} zum Sortieren.
     *
     * @param count die Anzahl n der am häufigsten gebuchten Seats
     * @return geordnete Liste von Seats, der häufigste Sitz an .get(0)
     */
    public List<Seat> topUsedSeats(int count) {
        // Hinweis: Die folgende Zeile generiert eine Map, die pro Seat alle Tickets enthält.
        // Sie dürfen diese Map verwenden.
        Map<Seat, List<Ticket>> ticketsOfSeat = registeredTickets.stream()
                .collect(Collectors.groupingBy(Ticket::getSeat));
        return null; // TODO implementieren
    }

}
