package ch.zhaw.prog2.ticketing.functional.handout;

import java.util.Objects;

/**
 *
 * This class combines a counter with an object of type T and provides
 * the {@link Comparable} interface that uses the natural order of the counter.
 * Thus, it can be used to compare and/or sort objects by the given counter.
 * <p>
 * An example of how this class can be used is shown in the test class
 * and here:
 * <pre>
 *     printStringListByLength(List.of("AC/DC", "Guns N' Roses", "Primus", "Blackmail"));
 *     private List<String> sortStringListByLength(List<String> strings) {
 *         Objects.requireNonNull(strings);
 *         List<ObjectCounter<String>> objectCounters = new ArrayList<>();
 *         for (String string : strings) {
 *             objectCounters.add (new ObjectCounter<>(string, string.length()));
 *         }
 *         Collections.sort(objectCounters);
 *         List<String> sortedList = new ArrayList<>();
 *         for (ObjectCounter<String> objectCounter : objectCounters) {
 *             String string = objectCounter.getObject();
 *             sortedList.add(string);
 *         }
 *         return sortedList;
 *     }
 * </pre>
 * generates the following output:
 * <pre>
 * (13) Guns N' Roses
 * ( 9) Blackmail
 * ( 6) Primus
 * ( 5) AC/DC
 * </pre>
 *
 */
public class ObjectCounter<T> implements Comparable<ObjectCounter<T>> {
    private final T object;
    private final long counter;

    public ObjectCounter (T object, long counter) {
        this.object = Objects.requireNonNull(object);
        this.counter = counter;
    }

    public T getObject() {
        return object;
    }

    @Override
    public int compareTo(ObjectCounter<T> o) {
        return Long.compare(o.counter, counter);
    }

    @Override
    public boolean equals(Object o) {
        throw new IllegalStateException("should not be used because the compareTo implementation only uses the counter value");
    }

    @Override
    public int hashCode() {
        throw new IllegalStateException("should not be used because the compareTo implementation only uses the counter value");
    }

}
