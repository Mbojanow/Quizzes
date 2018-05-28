package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Answer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer;

    @Before
    public void setUp() {
        answer = Answer.builder().description("I am insane").isCorrect(false).question(null).build();
        answerRepository.save(answer);
    }

    @After
    public void tearDown() {
        answerRepository.delete(answer);
    }

    @Test
    @DirtiesContext
    public void shouldFindAnswerByNullQuestion() {
        final List<Answer> answersWithNullQuestion = answerRepository.findByQuestion(null);
        assertEquals(1, answersWithNullQuestion.size());
    }


}