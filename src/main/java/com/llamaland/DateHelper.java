package com.llamaland;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyy");
    public static boolean isWeekEnd(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek==DayOfWeek.SATURDAY || dayOfWeek==DayOfWeek.SUNDAY;
    }

    public static LocalDate plusWeekDays(LocalDate date, int days) {
        LocalDate result = date;
        while(days>0) {
            if (!isWeekEnd(result)) {
                days--;
            }
            result = result.plusDays(1);
        }
        return result;
    }
}
