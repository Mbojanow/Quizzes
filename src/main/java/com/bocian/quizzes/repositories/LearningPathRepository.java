package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearningPathRepository extends JpaRepository<LearningPath, String> {
}
