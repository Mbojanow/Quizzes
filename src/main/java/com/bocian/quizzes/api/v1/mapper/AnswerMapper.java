package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.controllers.v1.AnswerController;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    Answer answerDTOToAnswer(AnswerDTO answerDTO);

    default AnswerDTO answerToAnswerDTO(final Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerDTO answerDTO = new AnswerDTO();

        answerDTO.setId(answer.getId());
        answerDTO.setDescription(answer.getDescription());
        answerDTO.setIsCorrect(answer.getIsCorrect());
        answerDTO.setUrl(AnswerController.ANSWERS_BASE_URL + "/" + answer.getId());

        return answerDTO;
    }

    default Answer updateAnswerFromAnswerDTO(final AnswerDTO answerDTO, final Answer answer) throws ObjectNotValidException {
        if (answerDTO.getIsCorrect() != null) {
            answer.setIsCorrect(answerDTO.getIsCorrect());
        }

        if (answerDTO.getDescription() != null) {
            if (answerDTO.getDescription().length() > Answer.DESCRIPTION_MAX_LENGTH
                    || answerDTO.getDescription().length() < AnswerDTO.MIN_DESCRIPTION_LENGTH) {
                throw new ObjectNotValidException("Description length has to be between "
                        + AnswerDTO.MIN_DESCRIPTION_LENGTH
                        + " and " + Answer.DESCRIPTION_MAX_LENGTH);
            }
            answer.setDescription(answerDTO.getDescription());
        }

        return answer;
    }

}
