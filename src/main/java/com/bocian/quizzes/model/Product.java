package com.bocian.quizzes.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.bocian.quizzes.model.Product.PRODUCT_TABLE_NAME;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = PRODUCT_TABLE_NAME)
@Builder
public class Product extends BaseEntity {

    public static final String PRODUCT_TABLE_NAME = "PRODUCT";

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = Quiz.PRODUCTS_FIELD_NAME, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();
}
