package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;

import java.util.List;

public interface AnswerService {

    AnswerDTO getAnswerById(Long id) throws DbObjectNotFoundException;

    List<AnswerDTO> getAllAnswers();

    AnswerDTO createNewAnswer(AnswerDTO answerDTO);

    AnswerDTO saveAnswer(Long id, AnswerDTO answerDTO) throws DbObjectNotFoundException;

    AnswerDTO patchAnswer(Long id, AnswerDTO answerDTO) throws DbObjectNotFoundException;

    void deleteAnswerById(Long id) throws DbObjectNotFoundException;

    List<AnswerDTO> getUnassignedToQuestion();
}
