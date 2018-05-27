package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.bocian.quizzes.model.LearningPath.LEARNING_PATH_TABLE_NAME;

@EqualsAndHashCode(exclude = "quizzes")
@ToString(exclude = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = LEARNING_PATH_TABLE_NAME)
@Builder
public class LearningPath {

    public static final String LEARNING_PATH_TABLE_NAME = "LEARNING_PATH";
    public static final String LEARNING_PATH_TITLE_COL_NAME = "TITLE";

    @Id
    @Column(name = LEARNING_PATH_TITLE_COL_NAME)
    private String title;

    @OneToMany(mappedBy = Quiz.LEARNING_PATH_FIELD_NAME, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

    public void addQuiz(final Quiz quiz) {
        if (!quizzes.contains(quiz)) {
            quizzes.add(quiz);
        }
    }
}
