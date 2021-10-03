package com.llamaland;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BirthdayLineParserTest {
    @Test
    void validRowIsParsedCorrectly() {
        BirthdayEntry entry = BirthdayLineParser.parseLine("Brown,Bobby,10-11-1950,bobby.brown@ilovellamaland.com");
        assertEquals("Brown", entry.getLastName());
        assertEquals("Bobby", entry.getFirstName());
        assertEquals( LocalDate.of(1950, 11, 10), entry.getBirthday());
        assertEquals("bobby.brown@ilovellamaland.com", entry.getEmail());
    }

    @Test
    void secondValidRowIsParsedCorrectly() {
        BirthdayEntry entry = BirthdayLineParser.parseLine("O'Rourke,Betsy,28-02-1900,betsy@heyitsme.com");
        assertEquals("O'Rourke", entry.getLastName());
        assertEquals("Betsy", entry.getFirstName());
        assertEquals( LocalDate.of(1900, 2, 28), entry.getBirthday());
        assertEquals("betsy@heyitsme.com", entry.getEmail());
    }

    @Test
    void wrongColumnNumberThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> BirthdayLineParser.parseLine("O'Rourke,28-02-1900,betsy@heyitsme.com")
        );
        assertEquals("Unexpected number of columns: 3", e.getMessage());
    }

    @Test
    void wrongDateFormatThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> BirthdayLineParser.parseLine("O'Rourke,Betsy,1900-03-28,betsy@heyitsme.com")
        );
        assertEquals("Invalid date format: 1900-03-28", e.getMessage());
    }
}