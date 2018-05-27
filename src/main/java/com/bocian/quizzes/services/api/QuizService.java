package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.QuizDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;

import java.util.List;

public interface QuizService {

    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizByName(String name) throws DbObjectNotFoundException;
}
