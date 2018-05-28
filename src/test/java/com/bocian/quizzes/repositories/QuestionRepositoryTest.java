package com.bocian.quizzes.repositories;

import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Question;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void shouldFindQuestionWithFetchedAnswers() {
        final Question question = Question.builder()
                .type(QuestionType.TASK)
                .durationMinutes(17L)
                .difficulty(Difficulty.EXTREME)
                .description("Will this test pass?")
                .build();

        final Answer answer = Answer.builder().description("It will pass!").isCorrect(false).question(question).build();
        question.setAnswers(Sets.newHashSet(answer));

        questionRepository.save(question);
        answerRepository.save(answer);
        final Optional<Question> retrievedQuestion = questionRepository.findByIdWithAnswers(1L);

        // first make sure question is actually retrieved
        assertTrue(retrievedQuestion.isPresent());

        // then make sure answers are fetched too
        final Optional<Answer> retrievedAnswer = retrievedQuestion.get().getAnswers().stream().findAny();
        assertTrue(retrievedAnswer.isPresent());
        assertEquals(answer, retrievedAnswer.get());
    }

}