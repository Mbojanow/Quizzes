package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuestionSetDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;

import java.util.Set;

public interface QuestionService {

    QuestionDTO getQuestionById(Long id) throws DbObjectNotFoundException;

    Set<QuestionDTO> getAllQuestions();

    QuestionDTO createQuestion(QuestionDTO questionDTO);

    QuestionDTO saveQuestion(Long id, QuestionDTO questionDTO) throws DbObjectNotFoundException;

    QuestionDTO patchQuestion(Long id, QuestionDTO questionDTO) throws DbObjectNotFoundException, ObjectNotValidException;

    void deleteQuestion(Long id) throws DbObjectNotFoundException;
}
