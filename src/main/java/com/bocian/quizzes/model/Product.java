package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.bocian.quizzes.model.Product.PRODUCT_TABLE_NAME;

@EqualsAndHashCode(exclude = "quizzes")
@ToString(exclude = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = PRODUCT_TABLE_NAME)
@Builder
public class Product {

    public static final String PRODUCT_TABLE_NAME = "PRODUCT";
    public static final String PRODUCT_NAME_COL_NAME = "NAME";

    @Id
    @Column(name = PRODUCT_NAME_COL_NAME)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = Quiz.PRODUCTS_FIELD_NAME, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();
}
