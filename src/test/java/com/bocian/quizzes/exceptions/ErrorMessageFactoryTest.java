package com.bocian.quizzes.exceptions;

import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorMessageFactoryTest {

    @Test
    public void shouldCreateExpectedMessageFromNumericId() {
        assertEquals("Failed to find QWERTY with id 1",
                ErrorMessageFactory.createEntityObjectWithIdMissingMessage(1L, "QWERTY"));
    }

    @Test
    public void shouldCreateExpectedMessageFromStringId() {
        assertEquals("Failed to find ASD with id XYZ",
                ErrorMessageFactory.createEntityObjectWithIdMissingMessage("XYZ", "ASD"));
    }
}