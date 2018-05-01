package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.services.api.AnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AnswerControllerTest {

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(answerController)
                .setControllerAdvice(RestExceptionHandler.class, RestValidationExceptionHandler.class).build();
    }

    @Test
    public void shouldGetOkWhenGettingAllAnswers() throws Exception {
        final int page = 0;
        final int size = 3;
        final String expectedResponseValue = "{\"answers\":[{\"id\":1,\"description\":\"desc\",\"is_correct\":true," +
                "\"answer_url\":null},{\"id\":2,\"description\":\"desc2\",\"is_correct\":false,\"answer_url\":null}," +
                "{\"id\":3,\"description\":\"desc3\",\"is_correct\":false,\"answer_url\":\"someurl\"}]}";
        when(answerService.getAnswers(page, size)).thenReturn(Arrays.asList(
                new AnswerDTO(1L, "desc", true, null),
                new AnswerDTO(2L, "desc2", false, null),
                new AnswerDTO(3L, "desc3", false, "someurl")));

        final MvcResult result = mockMvc.perform(get(AnswerController.ANSWERS_BASE_URL
                + PageRequestParamsFactory.get(page, size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers", hasSize(3))).andReturn();
        assertEquals(expectedResponseValue, result.getResponse().getContentAsString());
    }

    @Test
    public void shouldGetOkWhenGettingExistingAnswer() throws Exception {
        when(answerService.getAnswerById(1L))
                .thenReturn(new AnswerDTO(1L, "no", false, AnswerController.ANSWERS_BASE_URL + "/1"));

        final MvcResult result = mockMvc.perform(get(AnswerController.ANSWERS_BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":1,\"description\":\"no\",\"is_correct\":false,\"answer_url\":\"/api/v1/answers/1\"}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void shouldGetCreatedWhenCreatingValidAnswer() throws Exception {
        final AnswerDTO returnedDTO = new AnswerDTO(2L, "someDesc", true, "mockedUrl");
        when(answerService.createNewAnswer(any()))
                .thenReturn(returnedDTO);

        final AnswerDTO requestedDTO = new AnswerDTO(null, "someDesc", true, "");
        mockMvc.perform(post(AnswerController.ANSWERS_BASE_URL).content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.description", is("someDesc")))
                .andExpect(jsonPath("$.is_correct", is(true)))
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void shouldGetBadRequestWhenRequestingNonNumericId() throws Exception {
        final String returnedMessage = mockMvc.perform(get(AnswerController.ANSWERS_BASE_URL + "/notANumber")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("Failed to convert value to number"));
    }

    @Test
    public void shouldGetBadRequestWhenGettingNonExistingAnswer() throws Exception {
        final String exceptionMsg = "SomeMSg";
        when(answerService.getAnswerById(0L)).thenThrow(new DbObjectNotFoundException(exceptionMsg));
        final String returnedMessage = mockMvc.perform(get(AnswerController.ANSWERS_BASE_URL + "/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString(exceptionMsg));
    }

    @Test
    public void shouldGetBadRequestWhenInsertingNotValidAnswerWithNullDescription() throws Exception {
        final AnswerDTO requestedDTO = new AnswerDTO(null, null, true, "someUrl");
        final String returnedMessage = mockMvc.perform(post(AnswerController.ANSWERS_BASE_URL).content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("description has to be specified"));
    }

    @Test
    public void shouldGetBadRequestWhenInsertingNotValidAnswerWithNullCorrect() throws Exception {
        final AnswerDTO requestedDTO = new AnswerDTO(null, "descSpecified", null, "someUrl");
        final String returnedMessage = mockMvc.perform(post(AnswerController.ANSWERS_BASE_URL).content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("You need to specify if answer is correct"));
    }

    @Test
    public void shouldGetOkWhenUpdatingValidAnswer() throws Exception {
        final AnswerDTO returnedDTO = new AnswerDTO(3L, "someDesc", true, "mockedUrl");
        when(answerService.createNewAnswer(any()))
                .thenReturn(returnedDTO);

        final AnswerDTO requestedDTO = new AnswerDTO(null, "someDesczzz", false, "");
        mockMvc.perform(put(AnswerController.ANSWERS_BASE_URL + "/3").content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetBadRequestWhenUpdatingInvalidAnswerWithNullDescription() throws Exception {
        final AnswerDTO requestedDTO = new AnswerDTO(null, null, true, "someUrl");
        final String returnedMessage = mockMvc.perform(put(AnswerController.ANSWERS_BASE_URL + "/3").content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("description has to be specified"));
    }

    @Test
    public void shouldGetBadRequestWhenUpdatingInvalidAnswerWithNullCorrect() throws Exception {
        final AnswerDTO requestedDTO = new AnswerDTO(null, "some desc", null, "someUrl");
        final String returnedMessage = mockMvc.perform(put(AnswerController.ANSWERS_BASE_URL + "/3").content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("You need to specify if answer is correct"));
    }

    @Test
    public void shouldGetBadRequestWhenUpdatingInvalidAnswerEmptyDescription() throws Exception {
        final AnswerDTO requestedDTO = new AnswerDTO(null, "", false, "someUrl");
        final String returnedMessage = mockMvc.perform(put(AnswerController.ANSWERS_BASE_URL + "/3").content(mapper.writeValueAsString(requestedDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("description cannot be empty"));
    }

    @Test
    public void shouldGetBadRequestWhenDeletingAnswerWithNan() throws Exception {
        final String returnedMessage = mockMvc.perform(delete(AnswerController.ANSWERS_BASE_URL + "/DefinatelyNotANumber")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(returnedMessage, containsString("Failed to convert value to number"));
    }

    @Test
    public void shouldGetNoContentWhenDeletingValidAnswer() throws Exception {
        mockMvc.perform(delete(AnswerController.ANSWERS_BASE_URL + "/17")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}