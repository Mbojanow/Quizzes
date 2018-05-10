package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.model.LearningPath;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LearningPathMapper {
    LearningPathMapper INSTANCE = Mappers.getMapper(LearningPathMapper.class);

    LearningPathDTO learningPathToLearningPathDTO(final LearningPath learningPath);

    LearningPath learningPathDTOToLearningPath(final LearningPathDTO learningPathDTO);
}
