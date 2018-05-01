package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.controllers.v1.ProductController;
import com.bocian.quizzes.model.Product;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProductMapperTest {

    ProductMapper mapper = ProductMapper.INSTANCE;

    @Test
    public void shouldMapProductDTOToProduct() {
        final String name = "Xizt";
        final String description = "jkaem";
        final ProductDTO productDTO = new ProductDTO(name, description, null);
        final Product product = mapper.productDTOToProduct(productDTO);
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
    }

    @Test
    public void shouldMapProductToProductDTO() {
        final String name = "USTILO";
        final String description = "NiKo";
        final String expectedUrl = ProductController.PRODUCTS_BASE_URL + "/" + name;
        final Product product = Product.builder().name(name).description(description).build();
        final ProductDTO productDTO = mapper.productToProductDTO(product);

        assertEquals(name, productDTO.getName());
        assertEquals(description, productDTO.getDescription());
        assertEquals(expectedUrl, productDTO.getUrl());
    }
}