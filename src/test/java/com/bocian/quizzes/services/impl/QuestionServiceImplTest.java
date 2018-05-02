package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.QuestionMapper;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Question;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import com.bocian.quizzes.services.api.QuestionService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QuestionServiceImplTest {

    private static final String DESCRIPTION = "someDescLongEnough";
    private static final Difficulty DIFFICULTY = Difficulty.IMPOSSIBRU;
    private static final Long DURATION = 3L;
    private static final String TAG = "XYZ";
    private static final QuestionType QUESTION_TYPE = QuestionType.SINGLE_CHOICE;


    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        questionService = new QuestionServiceImpl(questionRepository, answerRepository, QuestionMapper.INSTANCE);
    }

    @Test
    public void shouldGetQuestionById() throws DbObjectNotFoundException {
        when(questionRepository.findByIdWithAnswers(1L)).thenReturn(Optional.of(createGenericQuestion(1L)));
        final QuestionDTO questionDTO = questionService.getQuestionById(1L);
        assertIsGenericQuestionDTO(questionDTO);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenGettingNonExistingQuestionById() throws DbObjectNotFoundException {
        when(questionRepository.findByIdWithAnswers(any(Long.class))).thenReturn(Optional.empty());
        questionService.getQuestionById(1L);
    }

    @Test
    public void shouldGetAllQuestions() {
        when(questionRepository.findAllWithAnswers()).thenReturn(Arrays.asList(createGenericQuestion(1L),
                createGenericQuestion(2L)));

        final Set<QuestionDTO> questionDTOs = questionService.getAllQuestions();
        assertEquals(2L, questionDTOs.size());
        questionDTOs.forEach(this::assertIsGenericQuestionDTO);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetQuestions() {
        Page page = new PageImpl(Arrays.asList(createGenericQuestion(1L), createGenericQuestion(2L)));
        when(questionRepository.findAllWithAnswers(any(Pageable.class))).thenReturn(page);

        final Set<QuestionDTO> questionDTOs = questionService.getQuestions(0, 5);
        assertEquals(2L, questionDTOs.size());
        questionDTOs.forEach(this::assertIsGenericQuestionDTO);
    }

    @Test
    public void shouldCreateNewQuestion() {
        QuestionDTO questionDTO = createGenericQuestionDTO(2L);
        when(questionRepository.save(any())).thenReturn(createGenericQuestion(2L));
        questionDTO = questionService.createQuestion(questionDTO);
        assertIsGenericQuestionDTO(questionDTO);
    }

    @Test
    public void shouldUpdateExistingQuestion() throws DbObjectNotFoundException {
        final Question question = createGenericQuestion(1L);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        QuestionDTO questionDTO = createGenericQuestionDTO(1L);
        when(questionRepository.save(any())).thenReturn(question);
        questionDTO = questionService.saveQuestion(1L, questionDTO);
        assertIsGenericQuestionDTO(questionDTO);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenUpdatingNonExistingQuestion() throws DbObjectNotFoundException {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        questionService.saveQuestion(1L, createGenericQuestionDTO(1L));
    }

    @Test
    public void shouldPatchExistingQuestion() throws DbObjectNotFoundException, ObjectNotValidException {
        final Question question = createGenericQuestion(1L);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        QuestionDTO questionDTO = createGenericQuestionDTO(1L);
        when(questionRepository.save(any())).thenReturn(question);
        questionDTO = questionService.patchQuestion(1L, questionDTO);
        assertIsGenericQuestionDTO(questionDTO);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenPatchNonExistingQuestion() throws DbObjectNotFoundException, ObjectNotValidException {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        questionService.patchQuestion(1L, createGenericQuestionDTO(1L));
    }

    @Test
    public void shouldDeleteExistingQuestion() throws DbObjectNotFoundException {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(createGenericQuestion(1L)));
        questionService.deleteQuestion(1L);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWheDeletingNonExistingQuestion() throws DbObjectNotFoundException {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        questionService.deleteQuestion(1L);
    }

    @Test
    public void shouldAddExistingAnswer() throws DbObjectNotFoundException, InvalidRequestException {
        final Question question = createGenericQuestion(1L);
        question.setType(QuestionType.SINGLE_CHOICE);
        when(questionRepository.findByIdWithAnswers(1L)).thenReturn(Optional.of(question));
        when(answerRepository.findById(1L)).thenReturn(Optional.of(Answer.builder()
                .description("qwe").isCorrect(true).build()));
        questionService.addExistingAnswer(1L, 1L);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenTryingToAddAnswerToNonExistingQuestion() throws DbObjectNotFoundException, InvalidRequestException {
        when(questionRepository.findByIdWithAnswers(1L)).thenReturn(Optional.empty());
        questionService.addExistingAnswer(1L, 1L);
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldThrowWhenTryingToAddAnswerToTaskQuestion() throws DbObjectNotFoundException, InvalidRequestException {
        final Question question = createGenericQuestion(1L);
        question.setType(QuestionType.TASK);
        when(questionRepository.findByIdWithAnswers(1L)).thenReturn(Optional.of(question));
        questionService.addExistingAnswer(1L, 1L);
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldThrowWhenTryingToAddAnswerToSTRQuestion() throws DbObjectNotFoundException, InvalidRequestException {
        final Question question = createGenericQuestion(1L);
        question.setType(QuestionType.STR_ANSWER);
        when(questionRepository.findByIdWithAnswers(1L)).thenReturn(Optional.of(question));
        questionService.addExistingAnswer(1L, 1L);
    }

    @Test//(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenTryingToAddNonExistingAnswerToExistingQuestion() throws DbObjectNotFoundException, InvalidRequestException {
        // to do
    }

    private QuestionDTO createGenericQuestionDTO(final Long id) {
        return new QuestionDTO(id, QUESTION_TYPE, DURATION, DIFFICULTY, DESCRIPTION, TAG,
                Collections.emptySet());
    }

    private Question createGenericQuestion(final Long id) {
        final Question question = Question.builder()
                .description(DESCRIPTION)
                .difficulty(DIFFICULTY)
                .durationMinutes(DURATION)
                .tag(TAG)
                .type(QUESTION_TYPE)
                .answers(new HashSet<>()).build();
        question.setId(id);
        return question;
    }

    private void assertIsGenericQuestionDTO(final QuestionDTO questionDTO) {
        assertEquals(DESCRIPTION, questionDTO.getDescription());
        assertEquals(DIFFICULTY, questionDTO.getDifficulty());
        assertEquals(DURATION, questionDTO.getDurationInMinutes());
        assertEquals(TAG, questionDTO.getTag());
        assertEquals(QUESTION_TYPE, questionDTO.getType());
    }

}