package ch.zhaw.prog2.ticketing.functional.handout;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectCounterTest {

    @Test
    void sortStringsByLength() {
        List<String> expected = List.of("Guns N' Roses", "Blackmail", "Primus", "AC/DC");
        List<String> found = sortStringListByLength(List.of("AC/DC", "Guns N' Roses", "Primus", "Blackmail"));
        assertEquals(expected, found, "Bandnames should be ordered by string length");
    }

    /**
     * Sorts the given list of strings by length
     *
     * @param strings must not be null
     * @return list of strings, sorted by length, never null
     */
    private List<String> sortStringListByLength(List<String> strings) {
        Objects.requireNonNull(strings);
        List<ObjectCounter<String>> objectCounters = new ArrayList<>();
        for (String string : strings) {
            objectCounters.add(new ObjectCounter<>(string, string.length()));
        }
        Collections.sort(objectCounters);
        List<String> sortedList = new ArrayList<>();
        for (ObjectCounter<String> objectCounter : objectCounters) {
            String string = objectCounter.getObject();
            sortedList.add(string);
        }
        return sortedList;
    }

}