package com.llamaland;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Command line argument parser.
 *
 * The parse() and validate() methods need to be called in sequence.
 * Once the call validate() succeeds, the arguments are ready to use.
 */
public class ArgParser {
    private static final String HOWTO_MSG = String.join(System.lineSeparator(),
            "Usage: java -jar llamaland.jar <birthday-filename> [--blacklist=<blacklist-filename>] [--today=<today>]",
            "",
            "Reports the citizens with upcoming 100th birthdays. It is supposed to be run on weekdays.",
            "",
            "Options:",
            "\t--blacklist=<blacklist-filename>: use the given file to filter citizens who opted out from the email",
            "\t--today=<today>: today's date in dd-MM-yyy format. It is used as the date of the report, which is the current date by default."
    );

    public void printUsage() {
        System.out.println(HOWTO_MSG);
    }

    private String birthdayFilename;
    private String blacklistFilename = null;
    private String todayStr = null;
    private LocalDate today;

    public void parse(String[] args) throws ArgException {
        if (args.length < 1) {
            throw new ArgException("Missing argument");
        }
        birthdayFilename = args[0];

        for (int i = 1; i < args.length; i++) {
            String[] parts = args[i].split("=");
            if (parts.length != 2) {
                throw new ArgException("Invalid argument: "+args[i]);
            }
            switch (parts[0]) {
                case "--blacklist":
                    blacklistFilename = parts[1];
                    break;
                case "--today":
                    todayStr = parts[1];
                    break;
                default:
                    throw new ArgException("Unknown argument: "+args[i]);
            }
        }
    }

    public String getBirthdayFilename() {
        return birthdayFilename;
    }

    public String getBlacklistFileName() {
        return blacklistFilename;
    }

    public LocalDate getToday() {
        return today;
    }

    public void validate() throws ArgException {
        checkFileExists(birthdayFilename);
        if (blacklistFilename != null) {
            checkFileExists(blacklistFilename);
        }
        if (todayStr != null) {
            try {
                today = LocalDate.parse(todayStr, DateHelper.DATE_FORMATTER);
            } catch (DateTimeParseException exception) {
                throw new ArgException("Today's date is invalid: "+todayStr);
            }
        } else {
            today = LocalDate.now();
        }
    }

    private void checkFileExists(String fileName) throws ArgException {
        if (!Files.exists(Paths.get(fileName))) {
            throw new ArgException(String.format("Error: file \"%s\" does not exist", birthdayFilename));
        }
    }
}
