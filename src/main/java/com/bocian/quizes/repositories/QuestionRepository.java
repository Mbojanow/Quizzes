package com.bocian.quizes.repositories;

import com.bocian.quizes.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select q from Question q left join fetch q.answers a WHERE q.id = :id")
    Question findByIdWithAnswers(@Param("id") Long id);
}
