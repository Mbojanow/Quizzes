package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {
}
