package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.BaseEntity;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.services.api.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(final QuestionRepository questionRepository,
                               final AnswerRepository answerRepository,
                               final QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionDTO getQuestionById(Long id) throws DbObjectNotFoundException {
        final Question question = validateQuestionExistenceAndGetWithAnswers(id);
        log.debug("Question with id " + id + " requested");
        return questionMapper.questionToQuestionDTO(question);
    }

    @Override
    public Set<QuestionDTO> getAllQuestions() {
        log.debug("All questions requested");
        return questionRepository.findAllWithAnswers().stream()
                .map(questionMapper::questionToQuestionDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<QuestionDTO> getQuestions(final int page, final int size) {
        log.debug("Requesting questions");
        return questionRepository.findAllWithAnswers(PageRequest.of(page, size,
                Sort.Direction.ASC, BaseEntity.ID_PROPERTY)).stream()
                .map(questionMapper::questionToQuestionDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public QuestionDTO createQuestion(final QuestionDTO questionDTO) {
        final Question questionToInsert = questionMapper.questionDTOToQuestion(questionDTO);
        questionToInsert.setId(null);
        log.debug("Creating new question");
        return questionMapper.questionToQuestionDTO(questionRepository.save(questionToInsert));
    }

    @Override
    @Transactional
    public QuestionDTO saveQuestion(final Long id, final QuestionDTO questionDTO) throws DbObjectNotFoundException {
        validateQuestionExistenceAndGet(id);
        final Question updatedQuestion = questionMapper.questionDTOToQuestion(questionDTO);
        updatedQuestion.setId(id);
        log.debug("Updating question with id " + id);
        return questionMapper.questionToQuestionDTO(questionRepository.save(updatedQuestion));
    }

    @Override
    @Transactional
    public QuestionDTO patchQuestion(final Long id, final QuestionDTO questionDTO) throws DbObjectNotFoundException, ObjectNotValidException {
        final Question question = validateQuestionExistenceAndGet(id);
        questionDTO.setId(id);
        final Question updatedQuestion = questionRepository
                .save(questionMapper.updateQuestionFromQuestionDTO(questionDTO, question));
        updatedQuestion.setId(id);
        log.debug("Partially updating question with id " + id);
        return questionMapper.questionToQuestionDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(final Long id) throws DbObjectNotFoundException {
        final Question question = validateQuestionExistenceAndGet(id);
        log.debug("Deleting question with id " + id);
        questionRepository.delete(question);
    }

    @Override
    @Transactional
    public void addExistingAnswer(final Long answerId, final Long questionId) throws DbObjectNotFoundException, InvalidRequestException {
        final Question question = validateQuestionExistenceAndGetWithAnswers(questionId);
        if (!requiresSpecificAnswer(question)) {
            throw new InvalidRequestException("Question of type \"" + question.getType().name().toLowerCase() +
                    "\" cannot have a specific question attached");
        }

        final Answer answer = validateAnswerExistenceAndGet(answerId);

        if (question.getAnswers().contains(answer)) {
            throw new InvalidRequestException("Question has already answer with id " + answerId + " as possible answer");
        }

        if (answer.getIsCorrect() && question.getType() == QuestionType.SINGLE_CHOICE && question.getAnswers()
                .stream().anyMatch(Answer::getIsCorrect)) {
            throw new InvalidRequestException("Single choice answer can have only one correct answer");
        }

        log.debug("Adding answer " + answer + " to question " + questionId);
        question.addAnswer(answer);
        questionRepository.save(question);
    }

    private Question validateQuestionExistenceAndGet(final Long id) throws DbObjectNotFoundException {
        final Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent()) {
            throwQuestionNotFound(id);
        }
        return question.get();
    }

    private Question validateQuestionExistenceAndGetWithAnswers(final Long id) throws DbObjectNotFoundException {
        final Optional<Question> question = questionRepository.findByIdWithAnswers(id);
        if (!question.isPresent()) {
            throwQuestionNotFound(id);
        }
        return question.get();
    }

    private void throwQuestionNotFound(final Long id) throws DbObjectNotFoundException {
        throw new DbObjectNotFoundException(ErrorMessageFactory
                .createEntityObjectWithIdMissingMessage(id, Question.QUESTION_TABLE_NAME));
    }

    private Answer validateAnswerExistenceAndGet(final Long id) throws DbObjectNotFoundException {
        final Optional<Answer> answer = answerRepository.findById(id);
        if (!answer.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithIdMissingMessage(id, Answer.ANSWER_TABLE_NAME));
        }
        return answer.get();
    }

    private static boolean requiresSpecificAnswer(final Question question) {
        final QuestionType questionType = question.getType();
        return questionType == QuestionType.SINGLE_CHOICE || questionType == QuestionType.MULTI_CHOICE;
    }

}
