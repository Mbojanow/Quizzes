package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.mapper.QuizMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuizDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.model.Quiz;
import com.bocian.quizzes.repositories.QuizRepository;
import com.bocian.quizzes.services.api.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return quizRepository.getQuizzesWithLearningPathAndProducts().stream()
                .map(quizMapper::quizToQuizDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QuizDTO getQuizByName(String name) throws DbObjectNotFoundException {
        final Optional<Quiz> quiz = quizRepository.getQuizByNameWithLearningPathAndProducts(name);
        if (!quiz.isPresent()) {
            throwQuizNotFound(name);
        }
        return quizMapper.quizToQuizDTO(quiz.get());
    }

    @Override
    public Set<QuestionDTO> getQuestionsForQuiz(String quizName) throws DbObjectNotFoundException {
        Optional<Quiz> quizWithQuestions = quizRepository.getQuizByNameWithQuestions(quizName);
        if (!quizWithQuestions.isPresent()) {
            throwQuizNotFound(quizName);
        }
        return quizWithQuestions.get().getQuestions().stream().map(questionMapper::questionToQuestionDTO).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public QuizDTO createQuiz(final QuizDTO quizDTO) throws InvalidRequestException {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizDTO.getName());
        if (existingQuiz.isPresent()) {
            throw new InvalidRequestException("Quiz with given name already exists");
        }

        return quizMapper.quizToQuizDTO(quizRepository.save(quizMapper.quizDTOToQuiz(quizDTO)));
    }

    private void throwQuizNotFound(final String lookupName) throws DbObjectNotFoundException {
        throw new DbObjectNotFoundException(ErrorMessageFactory
                .createEntityObjectWithIdMissingMessage(lookupName, Quiz.QUIZ_TABLE_NAME));
    }
}
