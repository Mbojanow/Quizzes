package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;

import static com.bocian.quizzes.model.Answer.ANSWER_TABLE_NAME;

@EqualsAndHashCode(callSuper = true, exclude = "question")
@ToString(of = {"description", "isCorrect"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ANSWER_TABLE_NAME)
public class Answer extends BaseEntity {

    public static final String ANSWER_TABLE_NAME = "ANSWER";
    public static final String QUESTION_FIELD_NAME = "question";

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(1024)", nullable = false)
    private String description;

    @Column(name = "IS_CORRECT", nullable = false)
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = BaseEntity.ID_COLUMN_NAME)
    private Question question;
}
