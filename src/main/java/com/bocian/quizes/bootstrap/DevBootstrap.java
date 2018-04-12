package com.bocian.quizes.bootstrap;

import com.bocian.quizes.common.Difficulty;
import com.bocian.quizes.common.QuestionType;
import com.bocian.quizes.configuration.Profiles;
import com.bocian.quizes.model.Answer;
import com.bocian.quizes.model.Product;
import com.bocian.quizes.model.Question;
import com.bocian.quizes.repositories.AnswerRepository;
import com.bocian.quizes.repositories.ProductRepository;
import com.bocian.quizes.repositories.QuestionRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@Profile(Profiles.DEVELOPMENT)
@Slf4j
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public DevBootstrap(ProductRepository productRepository,
                        QuestionRepository questionRepository,
                        AnswerRepository answerRepository) {
        this.productRepository = productRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Dev bootstrap started. Initializing database.");
        saveProducts();
        saveCppQuestionsAndAnswers();

        //questionRepository.findByIdWithAnswers(7L).getAnswers().forEach(System.out::println);
    }

    private void saveProducts() {
        final Product product1 = Product.builder().name("cpp").description("C plus plus product").build();
        final Product product2 = Product.builder().name("java").description("Java product").build();

        productRepository.save(product1);
        productRepository.save(product2);
        log.info("Products saved. Number of products in database: {}.", productRepository.count());
    }

    private void saveCppQuestionsAndAnswers() {
        saveCppFirstQuestionAndAnswers();
        saveCppSecondQuestionAndAnswers();
        saveCppThirdQuestionAndAnswers();
    }

    private void saveCppFirstQuestionAndAnswers() {
        final Question question1 = Question.builder()
                .type(QuestionType.MULTI_CHOICE)
                .durationMinutes(2L)
                .difficulty(Difficulty.EASY)
                .description("When is 'virtual' keyword used in C++ programming language?")
                .build();

        final Answer answer1 = Answer.builder()
                .isCorrect(true)
                .description("To create polymorphic class methods")
                .build();

        final Answer answer2 = Answer.builder()
                .isCorrect(false)
                .description("To create polymorphic functions")
                .build();

        final Answer answer3 = Answer.builder()
                .isCorrect(true)
                .description("To do multiple class inheritance")
                .build();

        final Answer answer4 = Answer.builder()
                .isCorrect(false)
                .description("To do private class inheritance")
                .build();

        answerRepository.saveAll(Arrays.asList(answer1, answer2, answer3, answer4));
        question1.addAnswers(Sets.newHashSet(answer1, answer2, answer3, answer4));
        questionRepository.save(question1);
    }

    private void saveCppSecondQuestionAndAnswers() {
        final Question question2 = Question.builder()
                .type(QuestionType.SINGLE_CHOICE)
                .durationMinutes(1L)
                .difficulty(Difficulty.EASY)
                .description("Is multi inheritance supported in C++?")
                .build();

        final Answer answer1 = Answer.builder()
                .isCorrect(true)
                .description("Yes")
                .build();

        final Answer answer2 = Answer.builder()
                .isCorrect(false)
                .description("No")
                .build();

        answerRepository.saveAll(Arrays.asList(answer1, answer2));
        question2.addAnswer(answer1);
        question2.addAnswer(answer2);
        questionRepository.save(question2);
    }

    private void saveCppThirdQuestionAndAnswers() {
        final Question question3 = Question.builder()
                .type(QuestionType.STR_ANSWER)
                .durationMinutes(2L)
                .difficulty(Difficulty.EASY)
                .description("Describe why a circular dependency when using C++11 shared pointers can happen and what" +
                        " kind of pointer is used to break it?")
                .build();
        questionRepository.save(question3);
    }
}
