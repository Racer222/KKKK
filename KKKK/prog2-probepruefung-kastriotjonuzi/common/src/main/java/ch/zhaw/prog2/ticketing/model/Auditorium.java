package ch.zhaw.prog2.ticketing.model;

import ch.zhaw.prog2.ticketing.util.Observable;
import ch.zhaw.prog2.ticketing.util.Observer;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * The auditorium has a fixed seat arrangement and can host
 * different shows.
 */
public class Auditorium implements Observable<Show> {
    private final String name;
    private final Set<Entrance> entrances = new HashSet<>();

    private final Set<Seat> seats;

    private final Set<Show> shows = new HashSet<>();
    protected final Set<Ticket> registeredTickets = new HashSet<>();

    /**
     * Each auditorium object has a name and at least one entrance.
     * The default constructor does not create any seats.
     *
     * @param name The name of the auditorium, e.g., "Museum Lichtspiele"
     */
    public Auditorium(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        seats = new HashSet<>();
        entrances.add(new Entrance());
    }

    /**
     * Each auditorium object has a name and at least one entrance.
     * This constructors initializes the seats.
     *
     * @param name The name of the auditorium, e.g., "Museum Lichtspiele"
     * @param seatMap A set of all available seats
     */
    public Auditorium(String name, Set<Seat> seatMap) {
        Objects.requireNonNull(name);
        this.name = name;
        seats = seatMap;
        entrances.add(new Entrance());
    }


    /**
     * Try to register a ticket. If there is already a ticket for the same
     * show and seat in registeredTickets, throw an exception.
     *
     * @param ticket The ticket to be checked
     * @throws InvalidTicketException is thrown if a ticket for the same show and seat has already been registered.
     */
    public void bookTicket(Ticket ticket) throws InvalidTicketException {
        Objects.requireNonNull(ticket);
        if (isTicketRegistered(ticket)) {
            throw new InvalidTicketException();
        } else {
            registeredTickets.add(ticket);
            notifyObserver(ticket.getShow());
        }
    }

    /**
     * Un-books a ticket if it has already been registered. Does nothing otherwise.
     *
     * @param ticket
     */
    public void cancelTicket(Ticket ticket) {
        Objects.requireNonNull(ticket);
        Optional<Ticket> ticketPackage = findRegisteredTicket(ticket.getShow(), ticket.getSeat());
        if (ticketPackage.isPresent()) {
            registeredTickets.remove(ticketPackage.get());
            notifyObserver(ticket.getShow());
        }
    }

    /**
     * Check if a ticket has already been registered
     *
     * @param ticket
     * @return
     */
    public boolean isTicketRegistered(Ticket ticket) {
        Objects.requireNonNull(ticket);
        return findRegisteredTicket(ticket.getShow(), ticket.getSeat()).isPresent();
    }

    /**
     * Returns a ticket if it has been registered, otherwise, an empty optional
     *
     * @param show The show of the requested ticket
     * @param seat The seat of the requested ticket
     * @return true if the ticket has already been registered, false otherwise
     */
    private Optional<Ticket> findRegisteredTicket(Show show, Seat seat) {
        Objects.requireNonNull(show);
        Objects.requireNonNull(seat);
        for (Ticket registeredTicket : registeredTickets) {
            if (registeredTicket.getShow().equals(show) && registeredTicket.getSeat().equals(seat)) {
                return Optional.of(registeredTicket);
            }
        }
        return Optional.empty();
    }

    public Set<Entrance> getEntrances() {
        return entrances;
    }

    public String getName () {
        return name;
    }

    /**
     * Register the show at the auditorium.
     *
     * The same show can be registered multiple times
     *
     * @param show {@link Show} to be registered, must not be null.
     * @throws AuditoriumAlreadyOccupiedException if there is already a show booked at the same date and time
     */
    public void bookAuditoriumFor(Show show) throws AuditoriumAlreadyOccupiedException {
        Objects.requireNonNull(show, "null values not allowed for show");
        ZonedDateTime newDateTime = show.getDateTime();
        Optional<Show> conflictingShow = shows.stream()
                .filter(bookedShow -> newDateTime.isEqual(bookedShow.getDateTime()))
                .filter(bookedShow -> !bookedShow.equals(show))
                .findAny();
        if (conflictingShow.isPresent()) {
            throw new AuditoriumAlreadyOccupiedException(this, conflictingShow.get());
        }
        shows.add(show);
    }

    public Set<Show> getShows () {
        return Collections.unmodifiableSet(shows);
    }


    public Set<Ticket> getRegisteredTickets() {
        return Collections.unmodifiableSet(registeredTickets);
    }

    public Stream<Seat> getSeats() {
        return seats.stream();
    }

    /**
     * Get all registered {@link Ticket}s of the given {@link Show}.
     *
     * @param show must not be null
     * @return all tickets registered for this show, never null
     */
    public Stream<Ticket> getRegisteredTicketsFor(Show show) {
        Objects.requireNonNull(show);
        return registeredTickets.stream().filter(ticket -> Objects.equals(ticket.getShow(), show));
    }

    /*
     * Fields and Methods used to implement the Observable Interface
     */

    /**
     * Set of registered Observers to be notified.
     */
    private final Set<Observer<Show>> observers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void addObserver(Observer<Show> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Show> observer) {
        observers.remove(observer);
    }

    /**
     * Helper method notifying all the registered observers.
     * @param updatedShow Show whose context has changed (e.g. tickets for show booked)
     */
    private void notifyObserver(Show updatedShow) {
        synchronized (observers) {
            observers.forEach(observer -> observer.update(this, updatedShow));
        }
    }

}
