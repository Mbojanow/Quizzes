package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.AnswerDTO;
import com.bocian.quizzes.api.v1.model.AnswerListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.services.api.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(AnswerController.ANSWERS_BASE_URL)
public class AnswerController {

    public static final String ANSWERS_BASE_URL = "/api/v1/answers";

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerListDTO getAnswers(@RequestParam(value = "page") final int page,
                                    @RequestParam(value = "size") final int size) {
        return new AnswerListDTO(answerService.getAnswers(page, size));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO getAnswer(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        return answerService.getAnswerById(id);
    }

    @GetMapping(value = "/unassigned", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerListDTO getUnassigned() {
        return new AnswerListDTO(answerService.getUnassignedToQuestion());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDTO createNewAnswer(@Valid @RequestBody AnswerDTO answerDTO) {
        return answerService.createNewAnswer(answerDTO);
    }

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO updateAnswer(@PathVariable("id") final Long id, @Valid @RequestBody AnswerDTO answerDTO)
            throws DbObjectNotFoundException {
        return answerService.saveAnswer(id, answerDTO);
    }

    @PatchMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerDTO patchAnswer(@PathVariable("id") final Long id, @RequestBody AnswerDTO answerDTO)
            throws DbObjectNotFoundException, ObjectNotValidException {
        return answerService.patchAnswer(id, answerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable("id") final Long id) throws DbObjectNotFoundException {
        answerService.deleteAnswerById(id);
    }
}
