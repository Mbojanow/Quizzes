package com.bocian.quizes.repositories;

import com.bocian.quizes.model.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {
}
