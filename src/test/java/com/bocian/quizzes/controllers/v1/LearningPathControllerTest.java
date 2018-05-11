package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.services.api.LearningPathService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LearningPathControllerTest {

    @Mock
    private LearningPathService learningPathService;

    @InjectMocks
    private LearningPathController learningPathController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(learningPathController)
                .setControllerAdvice(RestExceptionHandler.class, RestValidationExceptionHandler.class).build();
    }

    @Test
    public void shouldGetOkWhenGettingAllAnswers() throws Exception {
        when(learningPathService.getAllPaths()).thenReturn(Arrays.asList(
                new LearningPathDTO("t1"),
                new LearningPathDTO("t2")));

        mockMvc.perform(get(LearningPathController.LEARNING_PATH_BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.learning_paths", hasSize(2)));
    }

    @Test
    public void shouldGetOkWhenGettingPathByTitle() throws Exception {
        final String title = "testTitle";
        when(learningPathService.getPathByTitle(title)).thenReturn(new LearningPathDTO(title));
        mockMvc.perform(get(LearningPathController.LEARNING_PATH_BASE_URL + "/" + title)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title)));
    }

    @Test
    public void shouldGetCreatedWhenCreatingValidPath() throws Exception {
        final String title = "BocianZPogórza";
        final LearningPathDTO learningPathDTO = new LearningPathDTO(title);
        when(learningPathService.createPath(learningPathDTO)).thenReturn(learningPathDTO);

        mockMvc.perform(post(LearningPathController.LEARNING_PATH_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(learningPathDTO)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(title)));
    }

    @Test
    public void shouldGetBadRequestWhenCreatingInvalidPath() throws Exception {
        final LearningPathDTO learningPathDTO = new LearningPathDTO("x");
        when(learningPathService.createPath(learningPathDTO)).thenReturn(learningPathDTO);

        mockMvc.perform(post(LearningPathController.LEARNING_PATH_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(learningPathDTO)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message", containsString("Title too short")));
    }

    @Test
    public void shouldGetNoContentWhenDeletingPath() throws Exception {
        final String title = "title";
        doNothing().when(learningPathService).deletePath(title);
        mockMvc.perform(delete(LearningPathController.LEARNING_PATH_BASE_URL + "/" + title))
                .andExpect(status().isNoContent());
    }
}