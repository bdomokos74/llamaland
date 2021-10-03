package com.llamaland;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateHelperTest {
    LocalDate DATE_2021_09_27 = LocalDate.of(2021, 9, 27);
    LocalDate DATE_2021_09_28 = LocalDate.of(2021, 9, 28);

    //   x                   y
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void plus5WeekDays_onWeekend() {
        LocalDate result = DateHelper.plusWeekDays(DATE_2021_09_27, 5);
        assertEquals(LocalDate.of(2021, 10, 2), result);
    }

    //       x                     y
    //  27  28  29  30  01  02 03  04  05 06  07  08  09  10
    // Mon Tue Wed Thu Fri Sat Sun Mon Tue Wed Thu Fri Sat Sun
    //
    @Test
    void plus5WeekDays_afterWeekend() {
        LocalDate result = DateHelper.plusWeekDays(DATE_2021_09_28, 5);
        assertEquals(LocalDate.of(2021, 10, 5), result);
    }
}