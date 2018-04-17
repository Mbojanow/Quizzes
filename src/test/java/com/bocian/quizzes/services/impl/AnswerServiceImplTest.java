package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.AnswerMapper;
import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.services.api.AnswerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerServiceImplTest {

    private AnswerService service;

    @Mock
    private AnswerRepository answerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AnswerServiceImpl(AnswerMapper.INSTANCE, answerRepository);
    }

    @Test
    public void shouldGetAllAnswers() {
        when(answerRepository.findAll()).thenReturn(Arrays.asList(
                Answer.builder().isCorrect(false).description("ans1").build(),
                Answer.builder().isCorrect(true).description("ans2").build()));

        assertEquals(2, service.getAllAnswers().size());
    }

    @Test
    public void shouldNotThrowWhenNoAnswersInDb() {
        when(answerRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.getAllAnswers().isEmpty());
    }

    @Test
    public void shouldFindAnswerById() throws DbObjectNotFoundException {
        final Long id = 3L;
        final String description = "desc";
        when(answerRepository.findById(id))
                .thenReturn(Optional.of(new Answer(description, true, null)));

        AnswerDTO answerDTO = service.getAnswerById(id);
        assertTrue(answerDTO.getIsCorrect());
        assertEquals(description, answerDTO.getDescription());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowDbObjectNotFoundExceptionWhenEntityNotFound() throws DbObjectNotFoundException {
        when(answerRepository.findById(any())).thenReturn(Optional.empty());
        service.getAnswerById(1337L);
    }
}