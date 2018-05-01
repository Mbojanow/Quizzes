package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

import static com.bocian.quizzes.model.Quiz.QUIZ_TABLE_NAME;

@EqualsAndHashCode(of = "name")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = QUIZ_TABLE_NAME)
@Builder
public class Quiz {

    public static final String QUIZ_TABLE_NAME = "QUIZ";
    public static final String QUIZ_NAME_COL_NAME = "NAME";
    public static final String QUIZZES_TO_PRODUCTS_TABLE_NAME = "QUIZZES_TO_PRODUCTS";
    public static final String PRODUCTS_FIELD_NAME = "products";
    public static final String LEARNING_PATH_FIELD_NAME = "learningPath";

    @Id
    @Column(name = QUIZ_NAME_COL_NAME)
    private String name;

    @OneToMany(mappedBy = Question.QUIZ_FIELD_NAME, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEARNING_PATH_NAME", referencedColumnName = LearningPath.LEARNING_PATH_TITLE_COL_NAME)
    private LearningPath learningPath;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = QUIZZES_TO_PRODUCTS_TABLE_NAME,
            joinColumns = @JoinColumn(name = "QUIZ_NAME", referencedColumnName = QUIZ_NAME_COL_NAME),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_NAME", referencedColumnName = Product.PRODUCT_NAME_COL_NAME))
    private List<Product> products;

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

    public void setProducts(final List<Product> products) {
        createEmptyProductsIfNull();
        this.products = products;
    }

    private void createEmptyProductsIfNull() {
        if (products == null) {
            products = new ArrayList<>();
        }
    }
}
