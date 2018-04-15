package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.repositories.ProductRepository;
import com.bocian.quizzes.repositories.QuizRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialQuizDataLoaderTest {

    @Autowired
    @Qualifier("QUIZ")
    private DataLoader dataLoader;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    public void shouldCreateSomeProducts() {
        dataLoader.save();
        assertThat(quizRepository.count(), greaterThan(0L));
    }

}