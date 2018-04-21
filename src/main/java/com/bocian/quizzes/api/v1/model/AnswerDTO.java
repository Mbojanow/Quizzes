package com.bocian.quizzes.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private Long id;

    private String description;

    @JsonProperty("is_correct")
    private Boolean isCorrect;

    @JsonProperty("answer_url")
    private String url;
}
