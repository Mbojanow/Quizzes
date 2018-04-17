package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.api.v1.model.AnswerListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.services.api.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AnswerController.ANSWERS_BASE_URL)
public class AnswerController {

    public static final String ANSWERS_BASE_URL = "/api/v1/answers";

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AnswerListDTO getAllAnswers() {
        return new AnswerListDTO(answerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO getAnswer(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        return answerService.getAnswerById(id);
    }
}
