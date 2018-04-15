package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.bocian.quizzes.model.LearningPath.LEARNING_PATH_TABLE_NAME;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = LEARNING_PATH_TABLE_NAME)
@Builder
public class LearningPath extends BaseEntity {

    public static final String LEARNING_PATH_TABLE_NAME = "LEARNING_PATH";

    @Column(name = "TITLE", unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = Quiz.LEARNING_PATH_FIELD_NAME, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();
}
