package com.llamaland;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BirthdayLineParser {

    public static BirthdayEntry parseLine(String line) {
        String[] columns = line.split(",");
        if (columns.length != 4) {
            throw new IllegalArgumentException("Unexpected number of columns: "+columns.length);
        }

        LocalDate birthday;
        try {
            birthday = LocalDate.parse(columns[2], DateHelper.DATE_FORMATTER);
        } catch(DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format: "+columns[2]);
        }

        return new BirthdayEntry(columns[0], columns[1], birthday, columns[3]);
    }
}
