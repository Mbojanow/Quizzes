package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
