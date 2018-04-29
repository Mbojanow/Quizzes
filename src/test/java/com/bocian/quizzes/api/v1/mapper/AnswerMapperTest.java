package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnswerMapperTest {

    private AnswerMapper answerMapper = AnswerMapper.INSTANCE;

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

    @Test
    public void shouldUpdateAnswerFromAnswerDTO() throws ObjectNotValidException {
        final String updatedDescription = "descriptionThatIsLongEnough";
        final AnswerDTO answerDTO = new AnswerDTO(1L, updatedDescription, true, "someurl");
        Answer answer = Answer.builder().isCorrect(false).description("oldDesc").build();
        answer = answerMapper.updateAnswerFromAnswerDTO(answerDTO, answer);

        assertTrue(answer.getIsCorrect());
        assertEquals(updatedDescription, answer.getDescription());
    }

    @Test(expected = ObjectNotValidException.class)
    public void shouldThrowWhenUpdatingWithTooShortDescription() throws ObjectNotValidException {
        final AnswerDTO answerDTO = new AnswerDTO(1L, "", true, "someurl");
        Answer answer = Answer.builder().isCorrect(false).description("oldDesc").build();
        answerMapper.updateAnswerFromAnswerDTO(answerDTO, answer);
    }

    @Test(expected = ObjectNotValidException.class)
    public void shouldThrowWhenUpdatingWithTooLongDescription() throws ObjectNotValidException {
        final AnswerDTO answerDTO = new AnswerDTO(1L, StringUtils.repeat("X", "",
                Answer.DESCRIPTION_MAX_LENGTH + 1), true, "someurl");
        Answer answer = Answer.builder().isCorrect(false).description("oldDesc").build();
        answerMapper.updateAnswerFromAnswerDTO(answerDTO, answer);
    }
}