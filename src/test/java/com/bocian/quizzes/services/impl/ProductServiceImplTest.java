package com.bocian.quizzes.services.impl;


import com.bocian.quizzes.api.v1.mapper.ProductMapper;
import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.controllers.v1.ProductController;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.model.Product;
import com.bocian.quizzes.repositories.ProductRepository;
import com.bocian.quizzes.services.api.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    private static final String DESCRIPTION = "test desc";
    private static final String NAME = "some name used in test";

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productRepository, ProductMapper.INSTANCE);
    }

    @Test
    public void shouldGetProductByName() throws DbObjectNotFoundException {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(Product.builder().description(DESCRIPTION).name(NAME).build()));
        final ProductDTO productDTO = productService.findProductByName(NAME);
        assertEquals(DESCRIPTION, productDTO.getDescription());
        assertEquals(NAME, productDTO.getName());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenGettingNonexistingProductByName() throws DbObjectNotFoundException {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        productService.findProductByName(NAME);
    }

    @Test
    public void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                Product.builder().description(DESCRIPTION).name(NAME).build(),
                Product.builder().description(DESCRIPTION).name(NAME).build()
        ));

        final List<ProductDTO> productDTOs = productService.getAllProducts();
        assertEquals(2, productDTOs.size());
        productDTOs.forEach(productDTO -> {
            assertEquals(DESCRIPTION, productDTO.getDescription());
            assertEquals(NAME, productDTO.getName());
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetProducts() {

        Page page = new PageImpl(Arrays.asList(
                Product.builder().description(DESCRIPTION).name(NAME).build(),
                Product.builder().description(DESCRIPTION).name(NAME).build()));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        final List<ProductDTO> productDTOs = productService.getProducts(0, 5);
        assertEquals(2, productDTOs.size());
        productDTOs.forEach(productDTO -> {
            assertEquals(DESCRIPTION, productDTO.getDescription());
            assertEquals(NAME, productDTO.getName());
        });
    }

    @Test
    public void shouldSaveProduct() throws DbObjectNotFoundException {
        when(productRepository.findById(NAME)).thenReturn(Optional.of(Product.builder()
                .name(NAME).description("asd").build()));
        ProductDTO productDTO = new ProductDTO(NAME, DESCRIPTION, "someUrlThatDoesNotMatter");
        when(productRepository.save(any())).thenReturn(Product.builder().name(NAME).description(DESCRIPTION).build());
        productDTO = productService.saveProduct(NAME, productDTO);
        assertEquals(DESCRIPTION, productDTO.getDescription());
        assertEquals(NAME, productDTO.getName());
        assertEquals(ProductController.PRODUCTS_BASE_URL + "/" + NAME, productDTO.getUrl());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenSaveProductWhenEntityOnbjectDoesNotExist() throws DbObjectNotFoundException {
        when(productRepository.findById(NAME)).thenReturn(Optional.empty());
        final ProductDTO productDTO = new ProductDTO(NAME, DESCRIPTION, "someUrlThatDoesNotMatter");
        productService.saveProduct(NAME, productDTO);
    }

    @Test
    public void shouldCreateProduct() throws InvalidRequestException {
        when(productRepository.findById(NAME)).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(Product.builder().name(NAME).description(DESCRIPTION).build());
        ProductDTO productDTO = new ProductDTO(NAME, DESCRIPTION, "someUrlThatDoesNotMatter");
        productDTO = productService.createProduct(productDTO);
        assertEquals(DESCRIPTION, productDTO.getDescription());
        assertEquals(NAME, productDTO.getName());
        assertEquals(ProductController.PRODUCTS_BASE_URL + "/" + NAME, productDTO.getUrl());
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldThrowWhenCreatingProductWithDuplicatedName() throws InvalidRequestException {
        when(productRepository.findById(NAME)).thenReturn(Optional.of(Product.builder().name(NAME).build()));
        ProductDTO productDTO = new ProductDTO(NAME, DESCRIPTION, "someUrlThatDoesNotMatter");
        productService.createProduct(productDTO);
    }

    @Test
    public void shouldDeleteExistingProduct() throws DbObjectNotFoundException {
        when(productRepository.findById(NAME)).thenReturn(Optional.of(Product.builder()
                .name(NAME).description("qwerty").build()));
        productService.deleteProduct(NAME);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenDeletingNonexistingProduct() throws DbObjectNotFoundException {
        when(productRepository.findById(NAME)).thenReturn(Optional.empty());
        productService.deleteProduct(NAME);
    }

}