package com.llamaland;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ArgParserTest {
    private static ArgParser argParser;

    @BeforeAll
    static void beforeAll() {
        argParser = new ArgParser();
    }

    @Test
    void testBirthdayFileArgumentReturned() throws ArgException {
        argParser.parse(new String[]{"abc"});
        assertEquals("abc", argParser.getBirthdayFilename());
    }

    @Test
    void testBirthdayFileArgumentMissing() {
        Exception e = assertThrows(ArgException.class,
                () -> argParser.parse(new String[]{})
        );
        assertEquals("Missing argument", e.getMessage());
    }

    @Test
    void testInvalidArgument() {
        Exception e = assertThrows(ArgException.class,
                () -> argParser.parse(new String[]{"abc", "thisiswrong"})
        );
        assertEquals("Invalid argument: thisiswrong", e.getMessage());
    }

    @Test
    void testUnknownArgument() {
        Exception e = assertThrows(ArgException.class,
                () -> argParser.parse(new String[]{"abc", "--thisiswrong=123"})
        );
        assertEquals("Unknown argument: --thisiswrong=123", e.getMessage());
    }

    @Test
    void testMissingBirthdayFile() {
        Exception e = assertThrows(ArgException.class,
            () -> {
                argParser.parse(new String[]{"abc"});
                argParser.validate();
            });
        assertEquals("Error: file \"abc\" does not exist", e.getMessage());
    }

    @Test
    void testInalidTodayFile() throws IOException {
        Path path = Files.createTempFile("sample", ".txt");
        File tempFile = path.toFile();
        tempFile.deleteOnExit();

        Exception e = assertThrows(ArgException.class,
            () -> {
                argParser.parse(new String[]{tempFile.getAbsolutePath(), "--today=1-01-1920"});
                argParser.validate();
            });
        assertEquals("Today's date is invalid: 1-01-1920", e.getMessage());
    }

    @Test
    void testMissingBlacklistFile() throws IOException {
        Path path = Files.createTempFile("sample", ".txt");
        File tempFile = path.toFile();
        tempFile.deleteOnExit();

        Exception e = assertThrows(ArgException.class,
            () -> {
                argParser.parse(new String[]{tempFile.getAbsolutePath(), "--blacklist=missingfile"});
                argParser.validate();
            });
        assertTrue(e.getMessage().contains(" does not exist"));
    }
}