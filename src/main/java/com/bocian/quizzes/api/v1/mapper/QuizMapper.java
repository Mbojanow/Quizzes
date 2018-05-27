package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.QuizDTO;
import com.bocian.quizzes.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuizMapper {
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    Quiz quizDTOToQuiz(QuizDTO quizDTO);

    QuizDTO quizToQuizDTO(Quiz quiz);
}
