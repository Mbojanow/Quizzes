package com.bocian.quizes.repositories;

import com.bocian.quizes.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
