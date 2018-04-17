package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.AnswerMapper;
import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.services.api.AnswerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerMapper answerMapper, AnswerRepository answerRepository) {
        this.answerMapper = answerMapper;
        this.answerRepository = answerRepository;
    }


    @Override
    @Transactional
    public AnswerDTO getAnswerById(final Long id) throws DbObjectNotFoundException {
        final Optional<Answer> answer = answerRepository.findById(id);
        if (!answer.isPresent()) {
            throw new DbObjectNotFoundException("Failed to find Answer with id " + id);
        }

        return answerMapper.answerToAnswerDTO(answer.get());
    }

    @Override
    @Transactional
    public List<AnswerDTO> getAllAnswers() {
        return answerRepository.findAll()
                .stream()
                .map(answerMapper::answerToAnswerDTO)
                .collect(Collectors.toList());
    }
}
