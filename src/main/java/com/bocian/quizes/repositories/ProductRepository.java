package com.bocian.quizes.repositories;

import com.bocian.quizes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
