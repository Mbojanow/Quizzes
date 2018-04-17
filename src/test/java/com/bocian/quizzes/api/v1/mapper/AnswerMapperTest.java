package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.model.Answer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnswerMapperTest {

    AnswerMapper answerMapper = AnswerMapper.INSTANCE;

    @Test
    public void shouldMapAnswerToAnswerDTO() {
        final boolean isCorrect = true;
        final String answerDescription = "This is correct answer";
        final Long answerId = 7L;
        final Answer answer = Answer.builder().isCorrect(isCorrect).description(answerDescription).build();
        answer.setId(answerId);

        final AnswerDTO answerDTO = answerMapper.answerToAnswerDTO(answer);

        assertTrue(answerDTO.getIsCorrect());
        assertEquals(answerDescription, answerDTO.getDescription());
        assertEquals(answerId, answer.getId());
    }
}