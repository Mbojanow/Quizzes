package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.model.Quiz;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.repositories.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Qualifier("QUIZ")
@Slf4j
public class InitialQuizDataLoader implements DataLoader {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public InitialQuizDataLoader(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void save() {
        log.info("Creating initial quizzes");
        createCppQuiz();
        createJavaQuiz();
    }

    private void createCppQuiz() {
        final List<Question> cppQuestions = questionRepository.findByTag(InitialCppRelatedDataLoader.CPP_TAG);
        if (cppQuestions.isEmpty()) {
            throw new RuntimeException("Cannot create initial cpp quiz with no questions! No questions found for tag: " +
                    InitialCppRelatedDataLoader.CPP_TAG);
        }

        final Quiz cppQuiz = Quiz.builder()
                .name("Do you know C++?")
                .build();

        cppQuiz.setQuestions(cppQuestions);
        quizRepository.save(cppQuiz);
    }

    private void createJavaQuiz() {
        final List<Question> javaQuestions = questionRepository.findByTag(InitialJavaRelatedDataLoader.JAVA_TAG);
        if (javaQuestions.isEmpty()) {
            throw new RuntimeException("Cannot create initial java quiz with no questions! No questions found for tag: " +
                    InitialJavaRelatedDataLoader.JAVA_TAG);
        }

        final Quiz javaQuiz = Quiz.builder()
                .name("Java quiz")
                .build();

        javaQuiz.setQuestions(javaQuestions);
        quizRepository.save(javaQuiz);
    }
}
