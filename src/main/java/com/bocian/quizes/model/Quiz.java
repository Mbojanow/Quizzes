package com.bocian.quizes.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static com.bocian.quizes.model.Quiz.QUIZ_TABLE_NAME;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = QUIZ_TABLE_NAME)
public class Quiz extends BaseEntity {

    public static final String QUIZ_TABLE_NAME = "QUIZ";

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = Question.QUIZ_FIELD_NAME, fetch = FetchType.LAZY)
    private Set<Question> questions;
}
