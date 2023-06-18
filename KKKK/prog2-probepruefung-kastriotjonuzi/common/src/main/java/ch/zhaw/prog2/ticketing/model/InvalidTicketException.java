package ch.zhaw.prog2.ticketing.model;

/**
 * This exception is used when handling invalid tickets,
 * e.g. unpaid tickets or already used tickets
 */
public class InvalidTicketException extends Exception {
    public InvalidTicketException(String message) {
        super(message);
    }

    public InvalidTicketException() {
        this("Ticket is not valid.");
    }
}
