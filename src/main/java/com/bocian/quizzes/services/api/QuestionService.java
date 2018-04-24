package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;

public interface QuestionService {

    QuestionDTO getQuestionById(Long id) throws DbObjectNotFoundException;
}
