package com.bocian.quizzes.api.v1.model;

import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    public static final long MIN_DURATION = 1;
    public static final int MIN_DESCRIPTION_LENGTH = 10;
    public static final int MIN_TAG_LENGTH = 2;

    private Long id;

    @NotNull(message = "type cannot be null")
    private QuestionType type;

    @JsonProperty("duration_in_minutes")
    @NotNull(message = "duration has to be specified")
    @Min(value = MIN_DURATION, message = "Duration has to be positive")
    private Long durationInMinutes;

    @NotNull(message = "Question's difficulty has to be provided")
    private Difficulty difficulty;

    @NotNull(message = "Question value is mandatory. You need to ask something, right?!")
    @Length(min = MIN_DESCRIPTION_LENGTH, message = "Question length too short! It has to be at least 10 characters long")
    private String description;

    @NotNull(message = "tag has to be specified")
    @Length(min = MIN_TAG_LENGTH, message = "Tag has to be at least 2 character long")
    private String tag;

    @JsonProperty("possible_answers")
    @Valid
    private Set<AnswerDTO> answers;
}
