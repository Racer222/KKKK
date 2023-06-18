package ch.zhaw.prog2.ticketing.util;

/**
 * Observer which can be registered with an Observable and will be notified, when Objects of type T change.
 *
 * @param <T> Type of Objects the observer is interested in.
 */
@FunctionalInterface
public interface Observer<T> {
    /**
     * Method called by the Observable, when an Object of type T changes.
     *
     * @param observable Observable sending the notification
     * @param value Object which has been changed
     */
    void update(Observable<T> observable, T value);
}
