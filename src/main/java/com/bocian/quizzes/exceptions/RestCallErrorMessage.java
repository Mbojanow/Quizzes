package com.bocian.quizzes.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestCallErrorMessage {

    @JsonProperty("error_message")
    private String message;
}
