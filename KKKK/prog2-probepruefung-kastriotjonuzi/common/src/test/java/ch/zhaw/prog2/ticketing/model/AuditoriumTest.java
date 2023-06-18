package ch.zhaw.prog2.ticketing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditoriumTest {
    Auditorium auditorium;

    @BeforeEach
    void setUp() {
        auditorium = new Auditorium("Eigersaal");
    }

    @Test
    void getShows() {
        assertEquals(0, auditorium.getShows().size(), "new auditorium has no registered shows");
    }

    @Test
    void bookAuditoriumFor() {
        ZonedDateTime firstDate = ZonedDateTime.of(2022, 6, 2, 18, 15, 0, 0, ZoneId.systemDefault());
        Show firstShow = mock(Show.class);
        when(firstShow.getDateTime()).thenReturn(firstDate);

        Show secondShow = mock(Show.class);
        ZonedDateTime otherDate = firstDate.plusDays(1);
        when(secondShow.getDateTime()).thenReturn(otherDate);

        assertDoesNotThrow(() -> {
            auditorium.bookAuditoriumFor(firstShow);
            auditorium.bookAuditoriumFor(secondShow);
        });

        assertDoesNotThrow(() -> auditorium.bookAuditoriumFor(firstShow), "Adding the same show twice is ok.");

        Show conflictingShow = mock(Show.class);
        when(conflictingShow.getDateTime()).thenReturn(firstDate);
        assertThrows(AuditoriumAlreadyOccupiedException.class,
                () -> auditorium.bookAuditoriumFor(conflictingShow), "show at the same date / time is not allowed");
    }
}