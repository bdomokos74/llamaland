package com.llamaland;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Formatting the report based on a list of birthdays.
 */
public class ReportGenerator {
    private final String REPORT_HEADER = "Birthday report generated for the King on %s";
    private final String NO_BIRTHDAYS = "There are no upcoming birthdays to report today.";

    private final LocalDate today;

    public ReportGenerator(LocalDate now) {
        today = now;
    }

    // TODO this could be refactored a bit further
    public void printReport(List<BirthdayEntry> birthdaysToReport) {
        Map<LocalDate, List<BirthdayEntry>> birthdays = birthdaysToReport.stream()
                .collect(Collectors.groupingBy(e -> e.getBirthday().plusYears(100)));
        List<LocalDate> dates = new ArrayList<>(birthdays.keySet());
        dates.sort(null);

        System.out.println(createHeader());

        if (birthdaysToReport.size() == 0) {
            System.out.println(NO_BIRTHDAYS);
        } else {
            for (LocalDate date : dates) {
                System.out.println(createDateHeader(date, birthdays.get(date).size()));
                birthdays.get(date).stream().map(this::createLine).forEach(System.out::println);
            }
        }
        System.out.println();
    }

    String createDateHeader(LocalDate date, int numItems) {
        String s = "s";
        String have = "have";
        if (numItems == 1) {
            s = "";
            have = "has";
        }
        String dateHeader = String.format("\nThe following %d citizen%s %s 100th birthday on %s:", numItems, s, have, date.format(DateHelper.DATE_FORMATTER));
        return dateHeader;
    }

    String createHeader() {
        String hdr = String.format(REPORT_HEADER, today.format(DateHelper.DATE_FORMATTER));
        return String.join("\n",
            hdr,
            String.format("%" + hdr.length() + "s", "").replace(" ", "-")
        );
    }

    String createLine(BirthdayEntry birthday) {
        return String.format("%20s - %s", birthday.getFullName(), birthday.getEmail());
    }
}
