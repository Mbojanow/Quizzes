package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@Qualifier("CPP")
@Slf4j
public class InitialCppRelatedDataLoader implements DataLoader {

    public static final String CPP_TAG = "CPP";

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public InitialCppRelatedDataLoader(final QuestionRepository questionRepository,
                                       final AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public void save() {
        log.info("Saving C++ questions and answers to the database");
        saveCppFirstQuestionAndAnswers();
        saveCppSecondQuestionAndAnswers();
        saveCppThirdQuestionAndAnswers();
    }

    private void saveCppFirstQuestionAndAnswers() {
        final Question question1 = Question.builder()
                .tag(CPP_TAG)
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
                .tag(CPP_TAG)
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
                .tag(CPP_TAG)
                .type(QuestionType.STR_ANSWER)
                .durationMinutes(2L)
                .difficulty(Difficulty.EASY)
                .description("Describe why a circular dependency when using C++11 shared pointers can happen and what" +
                        " kind of pointer is used to break it?")
                .build();
        questionRepository.save(question3);
    }
}
