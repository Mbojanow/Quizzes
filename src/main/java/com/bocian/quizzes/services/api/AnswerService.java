package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;

import java.util.List;

public interface AnswerService {

    AnswerDTO getAnswerById(Long id) throws DbObjectNotFoundException;

    List<AnswerDTO> getAllAnswers();

    List<AnswerDTO> getAnswers(int page, int size);

    AnswerDTO createNewAnswer(AnswerDTO answerDTO);

    AnswerDTO saveAnswer(Long id, AnswerDTO answerDTO) throws DbObjectNotFoundException;

    AnswerDTO patchAnswer(Long id, AnswerDTO answerDTO) throws DbObjectNotFoundException, ObjectNotValidException;

    void deleteAnswerById(Long id) throws DbObjectNotFoundException;

    List<AnswerDTO> getUnassignedToQuestion();
}
