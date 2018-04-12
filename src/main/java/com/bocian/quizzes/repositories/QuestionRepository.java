package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select q from Question q left join fetch q.answers a WHERE q.id = :id")
    Optional<Question> findByIdWithAnswers(@Param("id") Long id);
}
