package ch.zhaw.prog2.ticketing.concurrency.handout.ticketbooking;

import ch.zhaw.prog2.ticketing.model.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The available ticket for a given show and a given seat
 */
public class AvailableTicket implements Comparable<AvailableTicket> {
    public final UUID id;
    private final TicketFactory ticketFactory;
    private final Seat seat;
    private final Show show;
    private Lock lock = new ReentrantLock();

    /**
     * Creates an available ticket for a given show and a given seat
     * @param ticketFactory the ticket factory of the show for which the ticket is being issued
     * @param seat the seat for which the ticket is being issued
     */
    public AvailableTicket(TicketFactory ticketFactory, Seat seat) {
        id = UUID.randomUUID();
        this.ticketFactory = Objects.requireNonNull(ticketFactory);
        this.show = Objects.requireNonNull(ticketFactory.getShow());
        this.seat = Objects.requireNonNull(seat);
    }

    public Seat getSeat(){
        return seat;
    }

    public Show getShow(){
        return show;
    }

    /**
     * lock the available ticket to prevent mutual access
     */
    public void lock(){
        lock.lock();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * unlock the available ticket
     */
    public void unlock(){
        lock.unlock();
    }

    /**
     * Issues the ticket
     *
     * @param orderingPerson the person for whom the ticket is to be issued
     *
     * @return an empty optional if the ticket could not be issued; otherwise, the ticket wrapped in an Optional
     */
    public Optional<Ticket> issueTicket(Guest orderingPerson){
        return ticketFactory.createTicket(seat, orderingPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(show, seat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableTicket availableTicket = (AvailableTicket) o;
        return id == availableTicket.id;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", show.toString(), seat.toString());
    }

    @Override
    public int compareTo(AvailableTicket o) {
        Objects.requireNonNull(o, "never compare to null");
        return id.compareTo(o.id);
    }
}
