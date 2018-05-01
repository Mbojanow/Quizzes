package com.bocian.quizzes.api.v1.mapper;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.controllers.v1.ProductController;
import com.bocian.quizzes.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productDTOToProduct(ProductDTO productDTO);

    default ProductDTO productToProductDTO(final Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setUrl(ProductController.PRODUCTS_BASE_URL + "/" + product.getName());

        return productDTO;
    }
}
