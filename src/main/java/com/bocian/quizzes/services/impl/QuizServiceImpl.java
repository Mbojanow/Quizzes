package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.mapper.QuizMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuizDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.model.Quiz;
import com.bocian.quizzes.repositories.QuizRepository;
import com.bocian.quizzes.services.api.QuizService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;
    private final QuizRepository quizRepository;

    public QuizServiceImpl(final QuizMapper quizMapper, final QuestionMapper questionMapper,
                           final QuizRepository quizRepository) {
        this.quizMapper = quizMapper;
        this.questionMapper = questionMapper;
        this.quizRepository = quizRepository;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream().map(quizMapper::quizToQuizDTO).collect(Collectors.toList());
    }

    @Override
    public QuizDTO getQuizByName(String name) throws DbObjectNotFoundException {
        final Optional<Quiz> quiz = quizRepository.findById(name);
        if (!quiz.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithIdMissingMessage(name, Quiz.QUIZ_TABLE_NAME));
        }
        return quizMapper.quizToQuizDTO(quiz.get());
    }

    @Override
    public Set<QuestionDTO> getQuestionsForQuiz(String quizName) throws DbObjectNotFoundException {
        Optional<Quiz> quizWithQuestions = quizRepository.getQuizByNameWithQuestions(quizName);
        if (!quizWithQuestions.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithIdMissingMessage(quizName, Quiz.QUIZ_TABLE_NAME));
        }
        return quizWithQuestions.get().getQuestions().stream().map(questionMapper::questionToQuestionDTO).collect(Collectors.toSet());
    }
}
