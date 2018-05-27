package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.api.v1.model.AnswerListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.services.api.AnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"Answers"})
@RestController
@RequestMapping(AnswerController.ANSWERS_BASE_URL)
public class AnswerController {

    public static final String ANSWERS_BASE_URL = "/api/v1/answers";

    private final AnswerService answerService;

    public AnswerController(final AnswerService answerService) {
        this.answerService = answerService;
    }

    @ApiOperation(value = "Get a page of answers with provided size", notes = "page and size params have to be specified")
    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerListDTO getAnswers(@RequestParam(value = "page", defaultValue = "0") final int page,
                                    @RequestParam(value = "size", defaultValue = "50") final int size) {
        return new AnswerListDTO(answerService.getAnswers(page, size));
    }

    @ApiOperation(value = "Get an answer with given in", notes = "id has to be a positive numeric value")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO getAnswer(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        return answerService.getAnswerById(id);
    }

    @ApiOperation("Get all answers which do not have a question assigned")
    @GetMapping(value = "/unassigned", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerListDTO getUnassigned() {
        return new AnswerListDTO(answerService.getUnassignedToQuestion());
    }

    @ApiOperation(value = "Create new answer", notes = "Id and url in body are ignored")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDTO createNewAnswer(@Valid @RequestBody AnswerDTO answerDTO) {
        return answerService.createNewAnswer(answerDTO);
    }

    @ApiOperation(value = "Update an existing answer", notes = "Id and url in body are ignored")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO updateAnswer(@PathVariable("id") final Long id, @Valid @RequestBody AnswerDTO answerDTO)
            throws DbObjectNotFoundException {
        return answerService.saveAnswer(id, answerDTO);
    }

    @ApiOperation(value = "Partially update an existing answer", notes = "Id and url in body are ignored")
    @PatchMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO patchAnswer(@PathVariable("id") final Long id, @RequestBody AnswerDTO answerDTO)
            throws DbObjectNotFoundException, ObjectNotValidException {
        return answerService.patchAnswer(id, answerDTO);
    }

    @ApiOperation("Delete an existing answer not assigned to any question.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable("id") final Long id)
            throws DbObjectNotFoundException, InvalidRequestException {
        answerService.deleteAnswerById(id);
    }
}
