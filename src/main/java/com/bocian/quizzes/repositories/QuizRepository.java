package com.bocian.quizzes.repositories;

import com.bocian.quizzes.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, String> {

    @Query("select q from Quiz q left join fetch q.questions qs left join fetch qs.answers where q.name = :name")
    Optional<Quiz> getQuizByNameWithQuestions(@Param("name") String name);
}
