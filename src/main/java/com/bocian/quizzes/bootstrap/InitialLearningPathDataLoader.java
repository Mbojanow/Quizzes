package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.model.LearningPath;
import com.bocian.quizzes.repositories.LearningPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Qualifier("LEARNINGPATH")
@Slf4j
public class InitialLearningPathDataLoader implements DataLoader {

    private final LearningPathRepository learningPathRepository;

    public InitialLearningPathDataLoader(LearningPathRepository learningPathRepository) {
        this.learningPathRepository = learningPathRepository;
    }

    @Override
    @Transactional
    public void save() {
        learningPathRepository.save(LearningPath.builder().title("Development").build());
    }
}
