package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Question;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

public class QuestionMapperTest {

    private QuestionMapper questionMapper = QuestionMapper.INSTANCE;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldUpdateAllFieldOfQuestionFromQuestionDTO() throws ObjectNotValidException {
        Question question = new Question();
        question.setId(2L);
        final QuestionDTO validQuestionDTO = new QuestionDTO(2L, QuestionType.SINGLE_CHOICE, 5L
                , Difficulty.IMPOSSIBRU, "how are you?", "CPP", new HashSet<>());
        question = questionMapper.updateQuestionFromQuestionDTO(validQuestionDTO, question);
        assertEquals(questionMapper.questionDTOToQuestion(validQuestionDTO), question);
    }

    @Test
    public void shouldThrowWhenUpdatingWithDescriptionTooShort() throws ObjectNotValidException {
        expectedException.expect(ObjectNotValidException.class);
        expectedException.expectMessage(containsString("Description length too short"));
        Question question = new Question();
        final QuestionDTO invalidQuestionDTO = new QuestionDTO(4L, QuestionType.TASK, 3L,
                Difficulty.IMPOSSIBRU, "tooshort", "XXX", new HashSet<>());
        questionMapper.updateQuestionFromQuestionDTO(invalidQuestionDTO, question);
    }

    @Test
    public void shouldThrowWhenUpdatingWithDurationZero() throws ObjectNotValidException {
        expectedException.expect(ObjectNotValidException.class);
        expectedException.expectMessage(containsString("Duration has to be a positive value"));
        Question question = new Question();
        final QuestionDTO invalidQuestionDTO = new QuestionDTO(4L, QuestionType.TASK, 0L,
                Difficulty.IMPOSSIBRU, "just long enough", "yep", new HashSet<>());
        questionMapper.updateQuestionFromQuestionDTO(invalidQuestionDTO, question);
    }

    @Test
    public void shouldThrowWhenUpdatingTagThatIsTooShort() throws ObjectNotValidException {
        expectedException.expect(ObjectNotValidException.class);
        expectedException.expectMessage(containsString("Tag value too short"));
        Question question = new Question();
        final QuestionDTO invalidQuestionDTO = new QuestionDTO(4L, QuestionType.TASK, 5L,
                Difficulty.IMPOSSIBRU, "just long enough", "X", new HashSet<>());
        questionMapper.updateQuestionFromQuestionDTO(invalidQuestionDTO, question);
    }
}