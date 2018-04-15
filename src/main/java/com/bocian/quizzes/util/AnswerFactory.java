package com.bocian.quizzes.util;

import com.bocian.quizzes.model.Answer;

public class AnswerFactory {

    public static Answer createCorrectFromNumber(final Long value) {
        return Answer.builder()
                .description(String.valueOf(value))
                .isCorrect(true)
                .build();
    }

    public static Answer createIncorrectFromNumber(final Long value) {
        return Answer.builder()
                .description(String.valueOf(value))
                .isCorrect(false)
                .build();
    }
}
