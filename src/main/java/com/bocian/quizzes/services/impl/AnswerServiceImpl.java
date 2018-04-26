package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.AnswerMapper;
import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.services.api.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerMapper answerMapper, AnswerRepository answerRepository) {
        this.answerMapper = answerMapper;
        this.answerRepository = answerRepository;
    }

    @Override
    public AnswerDTO getAnswerById(final Long id) throws DbObjectNotFoundException {
        final Answer answer = validateExistenceAndGet(id);
        log.debug("Answer by id " + id + " requested");
        return answerMapper.answerToAnswerDTO(answer);
    }

    @Override
    public List<AnswerDTO> getAllAnswers() {
        log.debug("All answers requested");
        return answerRepository.findAll()
                .stream()
                .map(answerMapper::answerToAnswerDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnswerDTO createNewAnswer(final AnswerDTO answerDTO) {
        final Answer answer = answerMapper.answerDTOToAnswer(answerDTO);
        answer.setId(null);
        log.debug("Creating new answer");
        return answerMapper.answerToAnswerDTO(answerRepository.save(answer));
    }

    @Override
    @Transactional
    public AnswerDTO saveAnswer(final Long id, final AnswerDTO answerDTO) throws DbObjectNotFoundException {
        validateExistenceAndGet(id);
        Answer updatedAnswer = answerMapper.answerDTOToAnswer(answerDTO);
        updatedAnswer.setId(id);
        log.debug("Updating answer with id: " + id);
        updatedAnswer = answerRepository.save(updatedAnswer);
        return answerMapper.answerToAnswerDTO(updatedAnswer);
    }

    @Override
    @Transactional
    public AnswerDTO patchAnswer(final Long id, final AnswerDTO answerDTO) throws DbObjectNotFoundException,
            ObjectNotValidException {
        final Answer answer = validateExistenceAndGet(id);
        answerDTO.setId(id);
        log.debug("Partially updating answer with id: " + id);
        final Answer updatedAnswer = answerRepository.save(answerMapper.updateAnswerFromAnswerDTO(answerDTO, answer));
        return answerMapper.answerToAnswerDTO(updatedAnswer);
    }

    @Override
    @Transactional
    public void deleteAnswerById(final Long id) throws DbObjectNotFoundException {
        final Answer answer = validateExistenceAndGet(id);
        log.debug("Deleting answer with id: " + id);
        answerRepository.delete(answer);
    }

    @Override
    public List<AnswerDTO> getUnassignedToQuestion() {
        return answerRepository.findByQuestion(null).stream()
                .map(answerMapper::answerToAnswerDTO)
                .collect(Collectors.toList());
    }

    private Answer validateExistenceAndGet(final Long id) throws DbObjectNotFoundException {
        final Optional<Answer> answer = answerRepository.findById(id);
        if (!answer.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithNumericIdMissingMessage(id, Answer.ANSWER_TABLE_NAME));
        }
        return answer.get();
    }
}
