package com.bocian.quizzes.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.bocian.quizzes.model.LearningPath.LEARNING_PATH_TABLE_NAME;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = LEARNING_PATH_TABLE_NAME)
public class LearningPath extends BaseEntity {

    public static final String LEARNING_PATH_TABLE_NAME = "LEARNING_PATH";
    public static final String PRODUCT_FIELD_NAME = "product";

    @Column(name = "TITLE", unique = true, nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = BaseEntity.ID_COLUMN_NAME)
    private Product product;
}
