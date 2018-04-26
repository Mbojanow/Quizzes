package com.bocian.quizzes.api.v1.model;

import com.bocian.quizzes.model.Answer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    public static final int MIN_DESCRIPTION_LENGTH = 1;

    private Long id;

    @Length(min = MIN_DESCRIPTION_LENGTH, max = Answer.DESCRIPTION_MAX_LENGTH, message = "description cannot be empty")
    @NotNull(message = "description has to be specified")
    private String description;

    @JsonProperty("is_correct")
    @NotNull(message = "You need to specify if answer is correct")
    private Boolean isCorrect;

    @JsonProperty("answer_url")
    private String url;
}
