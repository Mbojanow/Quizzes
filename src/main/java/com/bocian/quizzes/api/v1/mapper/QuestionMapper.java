package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = AnswerMapper.class)
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mappings({
            @Mapping(target = "durationMinutes", source = "durationInMinutes")
    })
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    @Mappings({
            @Mapping(target = "durationInMinutes", source = "durationMinutes")
    })
    QuestionDTO questionToQuestionDTO(Question question);
}
