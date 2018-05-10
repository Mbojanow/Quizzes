package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.model.LearningPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LearningPathMapperTest {

    private LearningPathMapper mapper = LearningPathMapper.INSTANCE;

    @Test
    public void shouldMapLearningPathToLearningPathDTO() {
        final String title = "SomeTitle";
        final LearningPath learningPath = new LearningPath(title, null);
        final LearningPathDTO learningPathDTO = mapper.learningPathToLearningPathDTO(learningPath);
        assertEquals(title, learningPathDTO.getTitle());
    }

    @Test
    public void shouldMapLearningPathDTOToLearningPath() {
        final String title = "HelloMyName Is Suzie";
        final LearningPathDTO learningPathDTO = new LearningPathDTO(title);
        final LearningPath learningPath = mapper.learningPathDTOToLearningPath(learningPathDTO);
        assertEquals(title, learningPath.getTitle());
    }
}