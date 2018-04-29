package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.model.LearningPath;
import com.bocian.quizzes.model.Product;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.model.Quiz;
import com.bocian.quizzes.repositories.LearningPathRepository;
import com.bocian.quizzes.repositories.ProductRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.repositories.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("QUIZ")
@Slf4j
public class InitialQuizDataLoader implements DataLoader {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ProductRepository productRepository;
    private final LearningPathRepository learningPathRepository;

    public InitialQuizDataLoader(QuizRepository quizRepository,
                                 QuestionRepository questionRepository,
                                 ProductRepository productRepository,
                                 LearningPathRepository learningPathRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.productRepository = productRepository;
        this.learningPathRepository = learningPathRepository;
    }

    @Override
    @Transactional
    public void save() {
        log.info("Creating initial quizzes");
        Optional<LearningPath> learningPath = learningPathRepository.findById("Development");
        if (!learningPath.isPresent()) {
            throw new RuntimeException("Unable to find development learning path");
        }

        createCppQuiz(learningPath.get());
        createJavaQuiz(learningPath.get());
    }

    private void createCppQuiz(final LearningPath learningPath) {
        final List<Question> cppQuestions = questionRepository.findByTag(InitialCppRelatedDataLoader.CPP_TAG);
        if (cppQuestions.isEmpty()) {
            throw new RuntimeException("Cannot create initial cpp quiz with no questions! No questions found for tag: " +
                    InitialCppRelatedDataLoader.CPP_TAG);
        }

        Optional<Product> product = productRepository.findById("cpp");
        if (!product.isPresent()) {
            throw new RuntimeException("Failed to get cpp product!");
        }

        final Quiz cppQuiz = Quiz.builder()
                .name("Do you know C++?")
                .products(Collections.singletonList(product.get()))
                .learningPath(learningPath)
                .build();

        cppQuiz.setQuestions(cppQuestions);
        quizRepository.save(cppQuiz);
    }

    private void createJavaQuiz(final LearningPath learningPath) {
        final List<Question> javaQuestions = questionRepository.findByTag(InitialJavaRelatedDataLoader.JAVA_TAG);
        if (javaQuestions.isEmpty()) {
            throw new RuntimeException("Cannot create initial java quiz with no questions! No questions found for tag: " +
                    InitialJavaRelatedDataLoader.JAVA_TAG);
        }

        Optional<Product> product = productRepository.findById("java");
        if (!product.isPresent()) {
            throw new RuntimeException("Failed to get java product!");
        }

        final Quiz javaQuiz = Quiz.builder()
                .name("Java quiz")
                .products(Collections.singletonList(product.get()))
                .learningPath(learningPath)
                .build();

        javaQuiz.setQuestions(javaQuestions);
        quizRepository.save(javaQuiz);
    }
}
