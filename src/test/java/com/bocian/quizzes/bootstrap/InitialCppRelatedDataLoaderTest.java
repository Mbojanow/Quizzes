package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.repositories.QuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialCppRelatedDataLoaderTest {

    @Autowired
    @Qualifier("CPP")
    private DataLoader dataLoader;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void shouldCreateSomeQuestionsWithCPPTag() {
        dataLoader.save();
        assertThat(questionRepository.findByTag(InitialCppRelatedDataLoader.CPP_TAG).size(),
                greaterThan(0));
    }
}