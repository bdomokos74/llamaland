package com.llamaland;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class BirthdayEntry {
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final String email;

    public BirthdayEntry( String lastName, String firstName, LocalDate birthday, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.email = email;
    }

    public static BirthdayEntry from(String line) {
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

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirthdayEntry that = (BirthdayEntry) o;

        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        return birthday.equals(that.birthday);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + birthday.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" + firstName + ", " + lastName + ", " + birthday + '}';
    }
}
