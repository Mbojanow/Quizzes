package com.bocian.quizzes.services.api;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;

import java.util.List;

public interface ProductService {

    ProductDTO findProductByName(String name) throws DbObjectNotFoundException;

    List<ProductDTO> findAllProducts();

    ProductDTO createProduct(ProductDTO productDTO) throws InvalidRequestException;

    ProductDTO saveProduct(String name, ProductDTO productDTO) throws DbObjectNotFoundException;

    void deleteProduct(String name) throws DbObjectNotFoundException;

}
