package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuestionSetDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.services.api.QuestionService;
import org.springframework.stereotype.Service;

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
        final Optional<Question> question = questionRepository.findByIdWithAnswers(id);
        if (!question.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithNumericIdMissingMessage(id, Question.QUESTION_TABLE_NAME));
        }
        return questionMapper.questionToQuestionDTO(question.get());
    }

    @Override
    public Set<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::questionToQuestionDTO)
                .collect(Collectors.toSet());
    }

}
