package ch.zhaw.prog2.ticketing.util;

/**
 * Observable Interface which allows to register Observers wanting to be notified, when Objects of type T change.
 *
 * @param <T> Type of Objects the observer is interested in.
 */
public interface Observable<T> {
    /**
     * Registers the provided Observer.
     *
     * @param observer to be notified when Objects of type T change
     */
    public void addObserver(Observer<T> observer);

    /**
     * Remove the provided Observer.
     *
     * @param observer to be not be notified anymore when Objects of type T change
     */
    public void removeObserver(Observer<T> observer);
}
