package com.llamaland;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BirthdayProcessorTest {
    private BirthdayProcessor birthdayProcessor;

    private BirthdayEntry BOBBY_1921_10_02 = createEntry(1921, 10, 2);
    private BirthdayEntry BOBBY_1921_10_03 = createEntry(1921, 10, 3);
    private BirthdayEntry BOBBY_1921_10_04 = createEntry(1921, 10, 4);

    LocalDate DATE_2021_09_27 = LocalDate.of(2021, 9, 27);
    LocalDate DATE_2021_09_28 = LocalDate.of(2021, 9, 28);

    //   x                   1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportCitizenWhenBirthdayIn5WorkingDays() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = List.of(BOBBY_1921_10_02);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(1, reportToday.size());
        assertEquals(BOBBY_1921_10_02, reportToday.get(0));
    }

    //   x                      1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportCitizenWhenBirthdayIn5WorkingDays2() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = List.of(BOBBY_1921_10_03);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(1, reportToday.size());
        assertEquals(BOBBY_1921_10_03, reportToday.get(0));
    }

    //   x                         1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportCitizenWhenBirthdayIn5WorkingDays3() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = List.of(BOBBY_1921_10_04);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(1, reportToday.size());
        assertEquals(BOBBY_1921_10_04, reportToday.get(0));
    }

    //       x                     1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void dontReportCitizenWhenBirthdayInLessThan5WorkingDays() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_28);
        List<BirthdayEntry> birthdays = List.of(BOBBY_1921_10_04);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(0, reportToday.size());
    }

    //   x                   1  1   1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportAllCitizenWhenBirthdayIn5WorkingDays() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = List.of(BOBBY_1921_10_02, BOBBY_1921_10_03, BOBBY_1921_10_04);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(3, reportToday.size());
        assertTrue(reportToday.containsAll(birthdays));
    }

    //   x                                              21
    //  27  28  29  30  01  02  03  04  05  06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportCitizenWhenBirthdayIn10WorkingDays_aboveThreshold() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = createEntries(1921, 10, 9, 21);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(21, reportToday.size());
        assertTrue(reportToday.containsAll(birthdays));
    }

    //   x                                              20
    //  27  28  29  30  01  02  03  04  05  06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void reportCitizenWhenBirthdayIn10WorkingDays_belowThreshold() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = createEntries(1921, 10, 9, 20);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(0, reportToday.size());
    }

    //   x                         21
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void dontReportCitizenWhenBirthdayIn5WorkingDays_lotOfBirthdays() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        List<BirthdayEntry> birthdays = createEntries(1921, 10, 4, 21);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(0, reportToday.size());
    }

    //   x                   1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void dontReportCitizenWhenDuplicateEmails() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        BirthdayEntry bobby =  new BirthdayEntry("Brown", "Bobby",
                LocalDate.of(1921, 10, 2),
                "bobby.brown@ilovellamaland.com");
        BirthdayEntry alfredo =  new BirthdayEntry("von Tappo", "Alfredo",
                LocalDate.of(2011, 1, 11),
                "bobby.brown@ilovellamaland.com");

        List<BirthdayEntry> birthdays = List.of(bobby, alfredo);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Collections.emptySet());
        assertEquals(0, reportToday.size());
    }

    //   x                   1
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void dontReportCitizenWhenOnBlacklist() {
        birthdayProcessor = new BirthdayProcessor(DATE_2021_09_27);
        BirthdayEntry bobby =  new BirthdayEntry("Brown", "Bobby",
                LocalDate.of(1921, 10, 2),
                "bobby.brown@ilovellamaland.com");
        List<BirthdayEntry> birthdays = List.of(bobby);

        List<BirthdayEntry> reportToday = birthdayProcessor.generate(birthdays, Set.of("bobby.brown@ilovellamaland.com"));
        assertEquals(0, reportToday.size());
    }

    // Helpers

    private BirthdayEntry createEntry(int year, int month, int day) {
        return new BirthdayEntry("Brown", "Bobby",
                LocalDate.of(year, month, day),
                "bobby.brown"+day+"@ilovellamaland.com");
    }

    private List<BirthdayEntry> createEntries(int year, int month, int day, int n) {
        List<BirthdayEntry> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add( new BirthdayEntry("Brown", "Bobby",
                    LocalDate.of(year, month, day),
                    "bobby.brown" + i + "@ilovellamaland.com")
            );
        }
        return result;
    }
}
