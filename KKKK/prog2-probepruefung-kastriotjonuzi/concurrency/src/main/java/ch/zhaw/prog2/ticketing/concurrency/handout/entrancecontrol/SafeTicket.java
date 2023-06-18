package ch.zhaw.prog2.ticketing.concurrency.handout.entrancecontrol;

import ch.zhaw.prog2.ticketing.model.Guest;
import ch.zhaw.prog2.ticketing.model.Seat;
import ch.zhaw.prog2.ticketing.model.Show;
import ch.zhaw.prog2.ticketing.model.Ticket;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unused")
public class SafeTicket extends Ticket {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public SafeTicket(Show show, Seat seat, Guest owner) {
        super(show, seat, owner);
    }

    @Override
    public boolean isAllowedToEnter() {
        boolean result;

            result = super.isAllowedToEnter();

        return result;
    }

    @Override
    public void useTicket() {




    }
}
