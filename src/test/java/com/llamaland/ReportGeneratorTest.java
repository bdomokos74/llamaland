package com.llamaland;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    LocalDate DATE_2021_09_27 = LocalDate.of(2021, 9, 27);
    LocalDate DATE_2021_09_28 = LocalDate.of(2021, 9, 28);
    private BirthdayEntry BOBBY_1921_10_02 = new BirthdayEntry("Brown", "Bobby",
            LocalDate.of(1921, 10, 2),
            "bobby.brown@ilovellamaland.com");

    ReportGenerator generator;
    @BeforeEach
    void setUp() {
        generator = new ReportGenerator(DATE_2021_09_27);
    }

    @Test
    void printDateHeaderDesc_single() {
        String dateHeader = generator.createDateHeader(DATE_2021_09_28, 1);
        assertEquals("\nThe following 1 citizen has 100th birthday on 28-09-2021:", dateHeader);
    }

    @Test
    void printDateHeaderDesc_multi() {
        String dateHeader = generator.createDateHeader(DATE_2021_09_28, 3);
        assertEquals("\nThe following 3 citizens have 100th birthday on 28-09-2021:", dateHeader);
    }

    @Test
    void printLine() {
        assertEquals("         Bobby Brown - bobby.brown@ilovellamaland.com", generator.createLine(BOBBY_1921_10_02));
    }
}