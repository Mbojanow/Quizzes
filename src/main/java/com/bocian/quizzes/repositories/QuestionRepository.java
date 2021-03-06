package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTag(final String tag);

    @Query(value = "select q from Question q left join fetch q.answers a WHERE q.id = :id")
    Optional<Question> findByIdWithAnswers(@Param("id") Long id);

    @Query(value = "select q from Question q join fetch q.answers")
    List<Question> findAllWithAnswers();

    @Query(value = "select q from Question q join fetch q.answers",
        countQuery = "select count(q) from Question q")
    Page<Question> findAllWithAnswers(Pageable pageable);
}
