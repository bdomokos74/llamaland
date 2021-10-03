package com.llamaland;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 */
public class BirthdayProcessor {
    private static final int DEFAULT_THRESHOLD = 20;
    private static final int AGE_FOR_EMAIL = 100;
    private static final int NOTICE_DAYS = 5;
    private static final int NOTICE_DAYS_ALOT = 10;

    private final LocalDate today;
    private final int noticeThreshold;

    public BirthdayProcessor(LocalDate today) {
        this(today, DEFAULT_THRESHOLD);
    }

    public BirthdayProcessor(LocalDate today, int noticeThreshold) {
        this.today = today;
        this.noticeThreshold = noticeThreshold;
    }

    public List<BirthdayEntry> generate(List<BirthdayEntry> birthdays, Set<String> blackList) {
        List<BirthdayEntry> noDuplicateEmails = removeDuplicates(birthdays);

        Map<LocalDate, List<BirthdayEntry>> birthdayMap = noDuplicateEmails.stream()
                .filter(e -> !blackList.contains(e.getEmail()))
                .filter(e -> {
                    LocalDate birthday = e.getBirthday().plusYears(AGE_FOR_EMAIL);
                    // We can ignore birthdays outside the next 20 days window
                    return !(birthday.isBefore(today) || birthday.isAfter(today.plusDays(20)));
                })
                .collect(Collectors.groupingBy(e -> e.getBirthday().plusYears(AGE_FOR_EMAIL)));

        ResultBuilder resultBuilder = new ResultBuilder(birthdayMap, today);
        return resultBuilder
                .with( NOTICE_DAYS, (lst) -> lst.size() <= noticeThreshold)
                .with( NOTICE_DAYS_ALOT, (lst) -> lst.size() > noticeThreshold)
                .build();
    }

    private List<BirthdayEntry> removeDuplicates(List<BirthdayEntry> birthdays) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (BirthdayEntry entry : birthdays) {
            String email = entry.getEmail();
            if (seen.contains(email)) {
                duplicates.add(email);
            }
            seen.add(email);
        }

        List<BirthdayEntry> result = new ArrayList<>();
        for (BirthdayEntry entry : birthdays) {
            if (!duplicates.contains(entry.getEmail())) {
                result.add(entry);
            }
        }
        return result;
    }

    static class ResultBuilder {
        private final Map<LocalDate, List<BirthdayEntry>> birthdayMap;
        private final List<BirthdayEntry> result;
        private final LocalDate today;

        public ResultBuilder(Map<LocalDate, List<BirthdayEntry>> birthdayMap, LocalDate today) {
            this.birthdayMap = birthdayMap;
            this.today = today;
            this.result = new ArrayList<>();
        }

        public ResultBuilder with(int noticeDays, Predicate<List<BirthdayEntry>> includeCriteria) {
            LocalDate targetDate = DateHelper.plusWeekDays(today, noticeDays);
            while (DateHelper.isWeekEnd(targetDate)) {
                checkAndAdd(targetDate, includeCriteria);
                targetDate = targetDate.plusDays(1);
            }
            checkAndAdd(targetDate, includeCriteria);
            return this;
        }

        private void checkAndAdd(LocalDate targetDate, Predicate<List<BirthdayEntry>> includeCriteria) {
            List<BirthdayEntry> currentBirthdays = birthdayMap.getOrDefault(targetDate, Collections.emptyList());
            if (includeCriteria.test(currentBirthdays)) {
                result.addAll(currentBirthdays);
            }
        }

        public List<BirthdayEntry> build() {
            return result;
        }
    }

}
