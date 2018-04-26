package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.services.api.QuestionService;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(final QuestionRepository questionRepository, final QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionDTO getQuestionById(Long id) throws DbObjectNotFoundException {
        final Question question = validateExistenceAndGet(id);
        return questionMapper.questionToQuestionDTO(question);
    }

    @Override
    public Set<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::questionToQuestionDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public QuestionDTO createQuestion(final QuestionDTO questionDTO) {
        final Question questionToInsert = questionMapper.questionDTOToQuestion(questionDTO);
        questionToInsert.setId(null);
        return questionMapper.questionToQuestionDTO(questionRepository.save(questionToInsert));
    }

    @Override
    @Transactional
    public QuestionDTO saveQuestion(final Long id, final QuestionDTO questionDTO) throws DbObjectNotFoundException {
        validateExistenceAndGet(id);
        final Question updatedQuestion = questionMapper.questionDTOToQuestion(questionDTO);
        updatedQuestion.setId(id);
        return questionMapper.questionToQuestionDTO(questionRepository.save(updatedQuestion));
    }

    @Override
    @Transactional
    public QuestionDTO patchQuestion(Long id, QuestionDTO questionDTO) throws DbObjectNotFoundException, ObjectNotValidException {
        final Question question = validateExistenceAndGet(id);
        questionDTO.setId(id);
        final Question updatedQuestion = questionRepository
                .save(questionMapper.updateQuestionFromQuestionDTO(questionDTO, question));
        updatedQuestion.setId(id);
        return questionMapper.questionToQuestionDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) throws DbObjectNotFoundException {
        final Question question = validateExistenceAndGet(id);
        questionRepository.delete(question);
    }

    private Question validateExistenceAndGet(final Long id) throws DbObjectNotFoundException {
        final Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithNumericIdMissingMessage(id, Question.QUESTION_TABLE_NAME));
        }
        return question.get();
    }

}
