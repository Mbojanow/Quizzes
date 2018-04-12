package com.bocian.quizes.repositories;

import com.bocian.quizes.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
