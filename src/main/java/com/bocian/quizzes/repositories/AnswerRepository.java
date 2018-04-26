package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Answer;
import com.bocian.quizzes.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion(final Question question);
}
