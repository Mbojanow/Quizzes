package com.bocian.quizzes.api.v1.model;

import com.bocian.quizzes.model.Answer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    @Min(value = 1, message = "Id has to be greater than 1")
    private Long id;

    @Length(min = 1, max = Answer.DESCRIPTION_MAX_LENGTH)
    private String description;

    @JsonProperty("is_correct")
    private Boolean isCorrect;

    @JsonProperty("answer_url")
    @Length(max = 0, message = "url is an ignored field and if provided, it's value should be empty for inserts and updates")
    private String url;
}
