package com.bocian.quizzes.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
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

    @JsonProperty("product_url")
    private String url;

    // this one has to be public so that DTOs that have ProductDTO indicated as @Valid can access this method!
    @AssertTrue(message = "Product name and description have to be different")
    public boolean isNameDifferentThanDescription() {
        return !name.equals(description);
    }
}
