package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.services.api.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.bocian.quizzes.controllers.v1.QuestionController.QUESTIONS_BASE_URL;

@RestController
@RequestMapping(QUESTIONS_BASE_URL)
public class QuestionController {
    public static final String QUESTIONS_BASE_URL = "/api/v1/questions";

    private final QuestionService questionService;

    public QuestionController(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO getQuestion(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        return questionService.getQuestionById(id);
    }
}
