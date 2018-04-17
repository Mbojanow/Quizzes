package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.model.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    AnswerDTO answerToAnswerDTO(Answer answer);
}
