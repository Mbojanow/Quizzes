package com.bocian.quizzes.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class QuestionSetDTO {
    private Set<QuestionDTO> questions;
}
