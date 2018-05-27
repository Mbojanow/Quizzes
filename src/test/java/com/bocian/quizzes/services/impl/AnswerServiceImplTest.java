package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.AnswerMapper;
import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.controllers.v1.AnswerController;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.services.api.AnswerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public void shouldGetAllAnswersWithCorrectUrl() {
        final Answer answer1 = Answer.builder().isCorrect(false).description("ans1").build();
        answer1.setId(1L);
        final Answer answer2 = Answer.builder().isCorrect(false).description("ans2").build();
        answer2.setId(2L);
        when(answerRepository.findAll()).thenReturn(Arrays.asList(answer1, answer2));

        final List<AnswerDTO> answerDTOs = service.getAllAnswers();
        assertEquals(2, answerDTOs.size());
        answerDTOs.forEach(answerDTO -> assertEquals(getAnswerUrl(answerDTO.getId()), answerDTO.getUrl()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetAllAnswersFromPageWithCorrectUrl() {
        final Answer answer1 = Answer.builder().isCorrect(false).description("ans1").build();
        answer1.setId(1L);
        final Answer answer2 = Answer.builder().isCorrect(false).description("ans2").build();
        answer2.setId(2L);
        Page page = new PageImpl(Arrays.asList(answer1, answer2));
        when(answerRepository.findAll(any(Pageable.class))).thenReturn(page);
        final List<AnswerDTO> answerDTOs = service.getAnswers(0, 2);
        assertEquals(2, answerDTOs.size());
        answerDTOs.forEach(answerDTO -> assertEquals(getAnswerUrl(answerDTO.getId()), answerDTO.getUrl()));
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

    @Test
    public void shouldCreateNewAnswer() {
        final Long id = 1L;
        final String description = "desc";
        final Answer savedAnswer = Answer.builder().description(description).isCorrect(true).build();
        savedAnswer.setId(id);
        when(answerRepository.save(any())).thenReturn(savedAnswer);
        AnswerDTO answerDTO = service.createNewAnswer(new AnswerDTO());

        assertEquals(id, answerDTO.getId());
        assertEquals(description, answerDTO.getDescription());
        assertTrue(answerDTO.getIsCorrect());
        assertEquals(getAnswerUrl(id), answerDTO.getUrl());
    }

    @Test
    public void shouldUpdateExistingAnswer() throws DbObjectNotFoundException {
        final Long id = 22L;
        final Answer answer = new Answer("17", false, null);
        answer.setId(id);

        when(answerRepository.findById(id)).thenReturn(Optional.of(answer));
        when(answerRepository.save(any())).thenReturn(answer);

        final AnswerDTO answerDTO = service.saveAnswer(id, new AnswerDTO());
        assertEquals(getAnswerUrl(id), answerDTO.getUrl());
        assertEquals(id, answerDTO.getId());
        assertEquals("17", answerDTO.getDescription());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowDuringAnswerUpdateWhenEntityNotFound() throws DbObjectNotFoundException {
        when(answerRepository.findById(any())).thenReturn(Optional.empty());
        service.saveAnswer(1L, null);
    }

    @Test
    public void shouldDeleteFoundAnswer() throws DbObjectNotFoundException, InvalidRequestException {
        when(answerRepository.findById(any())).thenReturn(Optional.of(new Answer()));
        service.deleteAnswerById(1L);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowDuringDeleteWhenEntityNotFound() throws DbObjectNotFoundException, InvalidRequestException {
        when(answerRepository.findById(any())).thenReturn(Optional.empty());
        service.deleteAnswerById(1L);
    }

    @Test
    public void shouldPatchAnswerCorrectlyWhenAllFieldsAreNull() throws DbObjectNotFoundException, ObjectNotValidException {
        final Long id = 953L;
        final AnswerDTO answerDTO = new AnswerDTO(null, null, null, null);
        final Answer answer = Answer.builder().description("SomethingDifferentThanUpdated")
                .isCorrect(false).build();
        answer.setId(id);

        when(answerRepository.findById(id)).thenReturn(Optional.of(answer));
        when(answerRepository.save(answer)).thenReturn(answer);

        final AnswerDTO patchedDTO = service.patchAnswer(id, answerDTO);
        assertEquals(getAnswerUrl(id), patchedDTO.getUrl());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowDuringPatchWhenEntityNotFound() throws DbObjectNotFoundException, ObjectNotValidException {
        when(answerRepository.findById(any())).thenReturn(Optional.empty());
        service.patchAnswer(1L, new AnswerDTO());
    }

    @Test
    public void shouldGetUnassignedToQuestion() {
        final String description = "HelloDescription";
        final Boolean correct = true;
        when(answerRepository.findByQuestion(null)).thenReturn(Collections.singletonList(Answer.builder()
                .description(description).isCorrect(correct).question(null).build()));
        final List<AnswerDTO> answersDTO = service.getUnassignedToQuestion();

        assertEquals(1, answersDTO.size());
        assertEquals(description, answersDTO.get(0).getDescription());
        assertEquals(correct, answersDTO.get(0).getIsCorrect());
    }

    private String getAnswerUrl(final Long id) {
        return AnswerController.ANSWERS_BASE_URL + "/" + id;
    }

}