package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, String> {
}
