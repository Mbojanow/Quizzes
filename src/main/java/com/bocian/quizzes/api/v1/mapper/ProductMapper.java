package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productDTOToProduct(ProductDTO productDTO);

    ProductDTO productToProductDTO(final Product product);
}
