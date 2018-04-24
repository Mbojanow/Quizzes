package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import com.bocian.quizzes.model.Question;

import java.util.stream.Collectors;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mappings({
            @Mapping(target = "durationMinutes", source = "durationInMinutes")
    })
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    default QuestionDTO questionToQuestionDTO(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionDTO questionDTO = new QuestionDTO();

        questionDTO.setDurationInMinutes( question.getDurationMinutes() );
        questionDTO.setType( question.getType() );
        questionDTO.setDifficulty( question.getDifficulty() );
        questionDTO.setDescription( question.getDescription() );
        questionDTO.setTag( question.getTag() );
        questionDTO.setAnswers(question.getAnswers().stream().map(AnswerMapper.INSTANCE::answerToAnswerDTO).collect(Collectors.toSet()));

        return questionDTO;
    }
}
