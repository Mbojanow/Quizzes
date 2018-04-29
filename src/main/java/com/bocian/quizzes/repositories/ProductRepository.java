package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
}
