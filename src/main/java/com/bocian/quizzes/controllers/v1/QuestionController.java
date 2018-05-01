package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.QuestionDTO;
import com.bocian.quizzes.api.v1.model.QuestionSetDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.services.api.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import static com.bocian.quizzes.controllers.v1.QuestionController.QUESTIONS_BASE_URL;

@Api(tags = "Questions")
@RestController
@RequestMapping(QUESTIONS_BASE_URL)
public class QuestionController {
    public static final String QUESTIONS_BASE_URL = "/api/v1/questions";

    private final QuestionService questionService;

    public QuestionController(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @ApiOperation("Get a question with all answers by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO getQuestion(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        return questionService.getQuestionById(id);
    }

    @ApiOperation(value = "Get a page of questions with provided size", notes = "page and size are mandatory")
    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionSetDTO getQuestions(@RequestParam(value = "page", defaultValue = "0") final int page,
                                       @RequestParam(value = "size", defaultValue = "10") final int size) {
        return new QuestionSetDTO(questionService.getQuestions(page, size));
    }

    @ApiOperation(value = "Create new question with possible answers")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO createQuestion(@Valid @RequestBody final QuestionDTO questionDTO) {
        detachAnswers(questionDTO);
        return questionService.createQuestion(questionDTO);
    }

    @ApiOperation("Update existing question")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO updateQuestion(@PathVariable("id") final Long id,
                                      @Valid @RequestBody final QuestionDTO questionDTO)
            throws DbObjectNotFoundException {
        return questionService.saveQuestion(id, questionDTO);
    }

    @ApiOperation("Update existing question")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestionDTO patchQuestion(@PathVariable("id") final Long id,
                                     @RequestBody final QuestionDTO questionDTO)
            throws DbObjectNotFoundException, ObjectNotValidException {
        return questionService.patchQuestion(id, questionDTO);
    }

    @ApiOperation("Delete existing question")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        questionService.deleteQuestion(id);
    }

    @ApiOperation(value = "Assign existing answer to a question")
    @PostMapping(value = "/{questionId}/answers/{answerId}")
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
