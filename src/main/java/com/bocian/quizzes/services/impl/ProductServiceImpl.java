package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.ProductMapper;
import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Product;
import com.bocian.quizzes.repositories.ProductRepository;
import com.bocian.quizzes.services.api.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(final ProductRepository productRepository,
                              final ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO findProductByName(final String name) throws DbObjectNotFoundException {
        final Product product = validateExistenceAndGet(name);
        log.debug("Product with name " + name + " requested");
        return productMapper.productToProductDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.debug("All products requested");
        return productRepository.findAll()
                .stream().map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProducts(final int page, final int size) {
        log.debug("Requesting products");
        return productRepository.findAll(PageRequest.of(page, size))
                .stream().map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) throws InvalidRequestException {
        final Optional<Product> productOptional = productRepository.findById(productDTO.getName());
        if (productOptional.isPresent()) {
            throw new InvalidRequestException("Product with same name already exists");
        }

        log.debug("Creating new product with name " + productDTO.getName());
        final Product product = productMapper.productDTOToProduct(productDTO);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(String name, ProductDTO productDTO) throws DbObjectNotFoundException {
        validateExistenceAndGet(name);
        final Product product = productMapper.productDTOToProduct(productDTO);
        product.setName(name);
        log.debug("Updating product: " + name);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(final String name) throws DbObjectNotFoundException {
        final Product product = validateExistenceAndGet(name);
        log.debug("Deleting product: " + name);
        productRepository.delete(product);
    }

    private Product validateExistenceAndGet(final String name) throws DbObjectNotFoundException {
        final Optional<Product> product = productRepository.findById(name);
        if (!product.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithIdMissingMessage(name, Product.PRODUCT_TABLE_NAME));
        }
        return product.get();
    }
}
