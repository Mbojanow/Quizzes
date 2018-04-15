package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.util.AnswerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Qualifier("JAVA")
@Slf4j
public class InitialJavaRelatedDataLoader implements DataLoader {

    public static final String JAVA_TAG = "JAVA";

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public InitialJavaRelatedDataLoader(final QuestionRepository questionRepository,
                                       final AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public void save() {
        log.info("Saving java initial questions and answers.");
        saveFirstJavaQuestionWithAnswers();
        saveSecondJavaQuestionAndAnswers();
    }

    private void saveFirstJavaQuestionWithAnswers() {
        final Question question = Question.builder()
                .tag(JAVA_TAG)
                .type(QuestionType.SINGLE_CHOICE)
                .durationMinutes(1L)
                .difficulty(Difficulty.NORMAL)
                .description("In which java version 'var' keyword was introduced?")
                .build();

        final Answer answer1 = AnswerFactory.createIncorrectFromNumber(7L);
        final Answer answer2 = AnswerFactory.createIncorrectFromNumber(8L);
        final Answer answer3 = AnswerFactory.createIncorrectFromNumber(9L);
        final Answer answer4 = AnswerFactory.createCorrectFromNumber(10L);

        final List<Answer> answers = Arrays.asList(answer1, answer2, answer3, answer4);
        answerRepository.saveAll(answers);
        question.addAnswers(answers);
        questionRepository.save(question);
    }

    private void saveSecondJavaQuestionAndAnswers() {
        final Question question = Question.builder()
                .tag(JAVA_TAG)
                .type(QuestionType.STR_ANSWER)
                .durationMinutes(15L)
                .difficulty(Difficulty.HARD)
                .description("Why java does not support multiple inheritance? " +
                        "Can you think of a scenario that would be problematic if java allowed multiple classes with extends keyword?")
                .build();
        questionRepository.save(question);
    }
}
