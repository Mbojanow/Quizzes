package com.bocian.quizzes.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LearningPathListDTO {

    @JsonProperty("learning_paths")
    private List<LearningPathDTO> learningPaths;
}
