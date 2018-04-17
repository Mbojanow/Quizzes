package com.bocian.quizzes.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerListDTO {

    private List<AnswerDTO> answers;
}
