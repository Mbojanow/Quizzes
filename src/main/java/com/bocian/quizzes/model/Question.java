package com.bocian.quizzes.model;

import com.bocian.quizzes.common.Difficulty;
import com.bocian.quizzes.common.QuestionType;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.bocian.quizzes.model.Question.QUESTION_TABLE_NAME;

@EqualsAndHashCode(callSuper = true, exclude = {"answers", "quiz"})
@Entity
@Table(name = QUESTION_TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    public static final String QUESTION_TABLE_NAME = "QUESTION";
    public static final String QUIZ_FIELD_NAME = "quiz";

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private QuestionType type;

    @Column(name = "DURATION", nullable = false)
    private Long durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIFFICULTY", nullable = false)
    private Difficulty difficulty;

    @Lob
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "TAG")
    private String tag;

    @OneToMany(mappedBy = Answer.QUESTION_FIELD_NAME, fetch = FetchType.LAZY)
    private Set<Answer> answers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "QUIZ_NAME", referencedColumnName = Quiz.QUIZ_NAME_COL_NAME)
    private Quiz quiz;

    public void setAnswers(final Set<Answer> answers) {
        // answers can be null if Question was created by lombok's builder
        createEmptyAnswersIfNull();
        if (answers != null) {
            answers.forEach(answer -> answer.setQuestion(this));
            this.answers = answers;
        }
    }

    public void addAnswer(final Answer answer) {
        answer.setQuestion(this);
        // answers can be null if Question was created by lombok's builder
        createEmptyAnswersIfNull();
        answers.add(answer);
    }

    public void addAnswers(final Collection<Answer> answersToQuestion) {
        answersToQuestion.forEach(this::addAnswer);
    }

    private void createEmptyAnswersIfNull() {
        if (answers == null) {
            answers = new HashSet<>();
        }
    }
}
