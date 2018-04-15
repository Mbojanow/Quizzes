package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.model.Product;
import com.bocian.quizzes.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Qualifier("PRODUCT")
@Slf4j
public class InitialProductDataLoader implements DataLoader {

    private final ProductRepository productRepository;

    public InitialProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void save() {
        log.info("Saving initial products to the database.");
        final Product product1 = Product.builder().name("cpp").description("C plus plus product").build();
        final Product product2 = Product.builder().name("java").description("Java product").build();

        productRepository.save(product1);
        productRepository.save(product2);
        log.info("Products saved. Number of products in database: {}.", productRepository.count());
    }
}
