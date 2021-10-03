package com.llamaland;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Entry point of the llamaland reporting application.
 */
public class Main {
    public static void main(String[] args) {
        ArgParser argParser = new ArgParser();
        try {
            argParser.parse(args);
            argParser.validate();
        } catch (ArgException exception) {
            argParser.printUsage();
            exception.printMessage();
            System.exit(1);
        }

        if(DateHelper.isWeekEnd(argParser.getToday())) {
            System.out.println("The King does not work on weekends. Please run this again on Monday.");
            System.exit(0);
        }

        List<BirthdayEntry> birthdays = readBirthdays(argParser.getBirthdayFilename());
        Set<String> blacklist = readBlacklist(argParser.getBlacklistFileName());

        BirthdayProcessor processor = new BirthdayProcessor(argParser.getToday());
        List<BirthdayEntry> birthdaysToReport = processor.generate(birthdays, blacklist);

        ReportGenerator generator = new ReportGenerator(argParser.getToday());
        generator.printReport(birthdaysToReport);
    }

    static List<BirthdayEntry> readBirthdays(String birthdayFilename) {
        List<BirthdayEntry> birthdays = null;

        try (Stream<String> lines = Files.lines(Paths.get(birthdayFilename))) {
            birthdays = lines.map(BirthdayEntry::from)
                    .collect(Collectors.toList());
        } catch( IllegalArgumentException illegalArgumentException) {
            System.out.printf("Error while processing %s - %s\n", birthdayFilename, illegalArgumentException.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.out.printf("Error during processing %s - %s\n", birthdayFilename, e.getMessage());
            System.exit(1);
        }
        return birthdays;
    }

    static Set<String> readBlacklist(String blacklistFilename) {
        if (blacklistFilename==null) return Collections.emptySet();

        try (Stream<String> lines = Files.lines(Paths.get(blacklistFilename))) {
            return lines.collect(Collectors.toSet());
        } catch (IOException e) {
            System.out.printf("Error during processing %s - %s%n", blacklistFilename, e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
