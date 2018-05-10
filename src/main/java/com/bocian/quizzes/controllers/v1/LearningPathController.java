package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.api.v1.model.LearningPathListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.services.api.LearningPathService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"LearningPaths"})
@RestController
@RequestMapping(LearningPathController.LEARNING_PATH_BASE_URL)
public class LearningPathController {

    public static final String LEARNING_PATH_BASE_URL = "/api/v1/learningpaths";

    private final LearningPathService learningPathService;

    public LearningPathController(LearningPathService learningPathService) {
        this.learningPathService = learningPathService;
    }

    @ApiOperation("Get list of all learning paths")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LearningPathListDTO getAllPaths() {
        return new LearningPathListDTO(learningPathService.getAllPaths());
    }

    @ApiOperation("Find learning by title")
    @GetMapping(value = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LearningPathDTO getPathByTitle(@PathVariable("title") final String title) throws DbObjectNotFoundException {
        return learningPathService.getPathByTitle(title);
    }

    @ApiOperation("Create new learning path")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LearningPathDTO createPath(@Valid @RequestBody final LearningPathDTO learningPathDTO)
            throws InvalidRequestException {
        return learningPathService.createPath(learningPathDTO);
    }

    @ApiOperation("Delete existing learning path")
    @DeleteMapping("/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePath(@PathVariable("title") final String title) throws DbObjectNotFoundException {
        learningPathService.deletePath(title);
    }


}
