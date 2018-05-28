package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.bocian.quizzes.services.api.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest {

    private static final String DESCRIPTION = "ThisIsDummyDescription";
    private static final Difficulty DIFFICULTY = Difficulty.EXTREME;
    private static final Integer DURATION = 17;
    private static final String TAG = "DummyTag";
    private static final QuestionType TYPE = QuestionType.SINGLE_CHOICE;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;
    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).
                setControllerAdvice(RestExceptionHandler.class, RestValidationExceptionHandler.class).build();
    }

    @Test
    public void shouldGetOkWhenGettingQuestionById() throws Exception {
        final QuestionDTO questionDTO = createDummyQuestionDTO(1L);
        questionDTO.getAnswers().add(new AnswerDTO(1L, "desc", true, "asd"));
        when(questionService.getQuestionById(1L)).thenReturn(questionDTO);
        mockMvc.perform(get(QuestionController.QUESTIONS_BASE_URL + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.difficulty", is(DIFFICULTY.toString())))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.duration_in_minutes", is(DURATION)))
                .andExpect(jsonPath("$.tag", is(TAG)))
                .andExpect(jsonPath("$.possible_answers", hasSize(1)))
                .andExpect(jsonPath("$.type", is(TYPE.toString())));
    }

    @Test
    public void shouldGetOkWhenGettingQuestions() throws Exception {
        when(questionService.getQuestions(0, 10)).thenReturn(Sets.newHashSet(createDummyQuestionDTO(1L),
                createDummyQuestionDTO(2L)));
        mockMvc.perform(get(QuestionController.QUESTIONS_BASE_URL + PageRequestParamsFactory.get(0, 10))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    public void shouldGetCreatedWhenCreatingValidQuestion() throws Exception {
        final QuestionDTO questionDTO = createDummyQuestionDTO(1L);
        when(questionService.createQuestion(questionDTO)).thenReturn(questionDTO);
        mockMvc.perform(post(QuestionController.QUESTIONS_BASE_URL).content(mapper.writeValueAsString(questionDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.difficulty", is(DIFFICULTY.toString())))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.duration_in_minutes", is(DURATION)))
                .andExpect(jsonPath("$.tag", is(TAG)))
                .andExpect(jsonPath("$.possible_answers", hasSize(0)))
                .andExpect(jsonPath("$.type", is(TYPE.toString())));
    }

    @Test
    public void shouldGetBadRequestWhenCreatingInvalidQuestion() throws Exception {
        final QuestionDTO questionDTO = new QuestionDTO(1L, null, 0L, null, "", "",
                new HashSet<>());

        mockMvc.perform(post(QuestionController.QUESTIONS_BASE_URL).content(mapper.writeValueAsString(questionDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message",
                        containsString("duration has to be positive")))
                .andExpect(jsonPath("$.error_message",
                        containsString("Question length too short")))
                .andExpect(jsonPath("$.error_message",
                        containsString("tag has to be at least 2 character long")))
                .andExpect(jsonPath("$.error_message",
                        containsString("Question's difficulty has to be provided")))
                .andExpect(jsonPath("$.error_message",
                        containsString("type cannot be null")));
    }

    @Test
    public void shouldUpdateExistingQuestion() {
        // to do
    }

    private QuestionDTO createDummyQuestionDTO(final Long id) {
        return QuestionDTO.builder().answers(new HashSet<>()).description(DESCRIPTION).difficulty(DIFFICULTY)
                .durationInMinutes(Long.valueOf(DURATION)).id(id).tag(TAG).type(TYPE).build();
    }
}