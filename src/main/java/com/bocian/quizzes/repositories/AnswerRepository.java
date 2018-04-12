package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
