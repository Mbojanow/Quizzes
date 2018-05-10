package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;

import java.util.List;

public interface LearningPathService {

    LearningPathDTO getPathByTitle(final String title) throws DbObjectNotFoundException;

    List<LearningPathDTO> getAllPaths();

    LearningPathDTO createPath(final LearningPathDTO learningPathDTO) throws InvalidRequestException;

    void deletePath(final String title) throws DbObjectNotFoundException;
}
