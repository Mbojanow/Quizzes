package com.bocian.quizzes.services.impl;

import com.bocian.quizzes.api.v1.mapper.LearningPathMapper;
import com.bocian.quizzes.api.v1.model.LearningPathDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.model.LearningPath;
import com.bocian.quizzes.repositories.LearningPathRepository;
import com.bocian.quizzes.services.api.LearningPathService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LearningPathServiceImplTest {

    private LearningPathService learningPathService;

    @Mock
    private LearningPathRepository learningPathRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        learningPathService = new LearningPathServiceImpl(learningPathRepository, LearningPathMapper.INSTANCE);
    }

    @Test
    public void shouldGetPathByTitle() throws DbObjectNotFoundException {
        final String title = "hello";
        when(learningPathRepository.findById(title)).thenReturn(Optional.of(new LearningPath(title, null)));
        final LearningPathDTO learningPathDTO = learningPathService.getPathByTitle(title);
        assertEquals(title, learningPathDTO.getTitle());
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenGettingNonExistingLearningPath() throws DbObjectNotFoundException {
        when(learningPathRepository.findById(any())).thenReturn(Optional.empty());
        learningPathService.getPathByTitle("WazzupMan?");
    }

    @Test
    public void shouldGetAllLearningPaths() {
        when(learningPathRepository.findAll()).thenReturn(Arrays.asList(
                LearningPath.builder().title("title1").build(),
                LearningPath.builder().title("title2").build()
        ));
        final List<LearningPathDTO> learningPaths = learningPathService.getAllPaths();
        assertTrue(learningPaths.stream().map(LearningPathDTO::getTitle).collect(Collectors.toList()).contains("title1"));
        assertTrue(learningPaths.stream().map(LearningPathDTO::getTitle).collect(Collectors.toList()).contains("title2"));
    }

    @Test
    public void shouldCreateValidLearningPath() throws InvalidRequestException {
        final String title = "validTitle";
        final LearningPath learningPath = new LearningPath(title, null);
        LearningPathDTO learningPathDTO = new LearningPathDTO(title);
        when(learningPathRepository.findById(title)).thenReturn(Optional.empty());
        when(learningPathRepository.save(learningPath)).thenReturn(learningPath);

        learningPathDTO = learningPathService.createPath(learningPathDTO);
        assertEquals(title, learningPathDTO.getTitle());
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldThrowWhenSavingPathWithDuplicatedTitle() throws InvalidRequestException {
        final String title = "stillValidTitle";
        final LearningPath learningPath = new LearningPath(title, null);
        LearningPathDTO learningPathDTO = new LearningPathDTO(title);
        when(learningPathRepository.findById(title)).thenReturn(Optional.of(learningPath));
        learningPathService.createPath(learningPathDTO);
    }

    @Test
    public void shouldDeleteExistingPath() throws DbObjectNotFoundException {
        final String title = "titleToDelete";
        final LearningPath learningPath = new LearningPath(title, null);
        when(learningPathRepository.findById(title)).thenReturn(Optional.of(learningPath));
        learningPathService.deletePath(title);
    }

    @Test(expected = DbObjectNotFoundException.class)
    public void shouldThrowWhenDeletingNonExistingPath() throws DbObjectNotFoundException {
        final String title = "IAmAGhostIDoNotExist";
        when(learningPathRepository.findById(title)).thenReturn(Optional.empty());
        learningPathService.deletePath(title);
    }

}