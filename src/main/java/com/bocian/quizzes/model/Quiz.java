package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

import static com.bocian.quizzes.model.Quiz.QUIZ_TABLE_NAME;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = QUIZ_TABLE_NAME)
@Builder
public class Quiz extends BaseEntity {

    public static final String QUIZ_TABLE_NAME = "QUIZ";

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = Question.QUIZ_FIELD_NAME, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public void setQuestions(final List<Question> questions) {
        // questions can be null if Quiz was created by lombok's builder
        createEmptyQuestionsIfNull();
        questions.forEach(question -> question.setQuiz(this));
        this.questions = questions;
    }

    public void addQuestion(final Question question) {
        question.setQuiz(this);
        // quiz can be null if Quiz was created by lombok's builder
        createEmptyQuestionsIfNull();
        questions.add(question);
    }

    public void addQuestions(final Collection<Question> questionsForQuiz) {
        questionsForQuiz.forEach(this::addQuestion);
    }

    private void createEmptyQuestionsIfNull() {
        // quiz can be null if Quiz was created by lombok's builder
        if (questions == null) {
            questions = new ArrayList<>();
        }
    }
}
