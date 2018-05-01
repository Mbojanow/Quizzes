package com.bocian.quizzes.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotNull(message = "Product name is mandatory")
    @Length(min = 2, message = "Product name needs to be at least 2 characters long")
    private String name;

    @NotNull(message = "Product description is mandatory")
    @Length(min = 10, message = "Product description too short. Has to be at least 10 characters long")
    private String description;
}
