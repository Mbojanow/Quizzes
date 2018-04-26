package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
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

    default Question updateQuestionFromQuestionDTO(final QuestionDTO questionDTO,
                                                   final Question question) throws ObjectNotValidException {
        if (questionDTO.getDescription() != null) {
            if (question.getDescription().length() < QuestionDTO.MIN_DESCRIPTION_LENGTH) {
                throw new ObjectNotValidException("Description length too short. Has to be at least: "
                        + QuestionDTO.MIN_DESCRIPTION_LENGTH);
            }
            question.setDescription(questionDTO.getDescription());
        }

        if (questionDTO.getDifficulty() != null) {
            question.setDifficulty(questionDTO.getDifficulty());
        }

        if (questionDTO.getDurationInMinutes() != null) {
            if (questionDTO.getDurationInMinutes() < QuestionDTO.MIN_DURATION) {
                throw new ObjectNotValidException("Duration has to be a positive value");
            }
            question.setDurationMinutes(questionDTO.getDurationInMinutes());
        }

        if (questionDTO.getType() != null) {
            questionDTO.setType(questionDTO.getType());
        }

        if (questionDTO.getTag() != null) {
            if (questionDTO.getTag().length() < QuestionDTO.MIN_TAG_LENGTH) {
                throw new ObjectNotValidException("Tag value too short. Has to be at least "
                        + QuestionDTO.MIN_TAG_LENGTH);
            }
            questionDTO.setTag(questionDTO.getTag());
        }

        return question;
    }
}
