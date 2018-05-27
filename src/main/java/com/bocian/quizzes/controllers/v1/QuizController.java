package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.QuestionSetDTO;
import com.bocian.quizzes.api.v1.model.QuizDTO;
import com.bocian.quizzes.api.v1.model.QuizListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.services.api.QuizService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.bocian.quizzes.controllers.v1.QuizController.QUIZZES_BASE_URL;

@Api(tags = "Quizzes")
@RestController
@RequestMapping(QUIZZES_BASE_URL)
public class QuizController {

    public static final String QUIZZES_BASE_URL = "/api/v1/quizzes";

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public QuizListDTO getAllQuizzes() {
        return new QuizListDTO(quizService.getAllQuizzes());
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public QuizDTO getQuizByName(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        return quizService.getQuizByName(name);
    }

    @GetMapping("/{name}/questions")
    @ResponseStatus(HttpStatus.OK)
    public QuestionSetDTO getQuizsQuestions(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        return new QuestionSetDTO(quizService.getQuestionsForQuiz(name));
    }
}
