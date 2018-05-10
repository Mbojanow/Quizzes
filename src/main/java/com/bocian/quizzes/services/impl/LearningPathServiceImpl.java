package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.LearningPathMapper;
import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.ErrorMessageFactory;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.model.LearningPath;
import com.bocian.quizzes.repositories.LearningPathRepository;
import com.bocian.quizzes.services.api.LearningPathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LearningPathServiceImpl implements LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final LearningPathMapper learningPathMapper;

    public LearningPathServiceImpl(final LearningPathRepository learningPathRepository,
                                   final LearningPathMapper learningPathMapper) {
        this.learningPathRepository = learningPathRepository;
        this.learningPathMapper = learningPathMapper;
    }

    @Override
    public LearningPathDTO getPathByTitle(final String title) throws DbObjectNotFoundException {
        final LearningPath learningPath = validateExistenceAndGet(title);
        log.debug("Learning path with title {} requested", title);
        return learningPathMapper.learningPathToLearningPathDTO(learningPath);
    }

    @Override
    public List<LearningPathDTO> getAllPaths() {
        log.debug("All paths requested");
        return learningPathRepository.findAll().stream()
                .map(learningPathMapper::learningPathToLearningPathDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LearningPathDTO createPath(final LearningPathDTO learningPathDTO) throws InvalidRequestException {
        Optional<LearningPath> existingLearningPath = learningPathRepository.findById(learningPathDTO.getTitle());
        if (existingLearningPath.isPresent()) {
            throw new InvalidRequestException("LearningPath with same title already exists");
        }

        log.debug("Creating new learning path - {}", learningPathDTO.getTitle());
        final LearningPath insertedLearningPath = learningPathRepository
                .save(learningPathMapper.learningPathDTOToLearningPath(learningPathDTO));
        return learningPathMapper.learningPathToLearningPathDTO(insertedLearningPath);
    }

    @Transactional
    @Override
    public void deletePath(final String title) throws DbObjectNotFoundException {
        final LearningPath learningPath = validateExistenceAndGet(title);
        log.debug("Deleting learning path with title {}", title);
        learningPathRepository.delete(learningPath);
    }

    private LearningPath validateExistenceAndGet(final String title) throws DbObjectNotFoundException {
        final Optional<LearningPath> learningPath = learningPathRepository.findById(title);
        if (!learningPath.isPresent()) {
            throw new DbObjectNotFoundException(ErrorMessageFactory
                    .createEntityObjectWithIdMissingMessage(title, LearningPath.LEARNING_PATH_TABLE_NAME));
        }
        return learningPath.get();
    }
}
