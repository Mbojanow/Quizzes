package com.bocian.quizzes.util;

import com.bocian.quizzes.model.Answer;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnswerFactoryTest {

    private static final Long NUM_TEST_VALUE = 5L;

    @Test
    public void shouldCreateCorrectAnswerFromLong() {
        final Answer answer = AnswerFactory.createCorrectFromNumber(NUM_TEST_VALUE);
        assertTrue(answer.getIsCorrect());
        assertEquals(String.valueOf(NUM_TEST_VALUE), answer.getDescription());
    }

    @Test
    public void shouldCreateIncorrectAnswerFromLong() {
        final Answer answer = AnswerFactory.createIncorrectFromNumber(NUM_TEST_VALUE);
        assertFalse(answer.getIsCorrect());
        assertEquals(String.valueOf(NUM_TEST_VALUE), answer.getDescription());
    }
}