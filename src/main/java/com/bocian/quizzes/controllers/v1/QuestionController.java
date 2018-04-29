package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuestionSetDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.services.api.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionSetDTO getAllQuestions() {
        return new QuestionSetDTO(questionService.getAllQuestions());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO createQuestion(@Valid @RequestBody final QuestionDTO questionDTO) {
        detachAnswers(questionDTO);
        return questionService.createQuestion(questionDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO updateQuestion(@PathVariable("id") final Long id,
                                      @Valid @RequestBody final QuestionDTO questionDTO)
            throws DbObjectNotFoundException {
        return questionService.saveQuestion(id, questionDTO);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO patchQuestion(@PathVariable("id") final Long id,
                                     @RequestBody final QuestionDTO questionDTO)
            throws DbObjectNotFoundException, ObjectNotValidException {
        return questionService.patchQuestion(id, questionDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        questionService.deleteQuestion(id);
    }

    @PostMapping(value = "/{questionId}/{answerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAnswerToQuestion(@PathVariable("questionId") final Long questionId,
                                    @PathVariable("answerId") final Long answerId)
            throws DbObjectNotFoundException, InvalidRequestException {
        questionService.addExistingAnswer(answerId, questionId);
    }

    private void detachAnswers(final QuestionDTO questionDTO) {
        if (questionDTO.getAnswers() != null) {
            questionDTO.getAnswers().forEach(answerDTO -> answerDTO.setId(null));
        }
    }
}
