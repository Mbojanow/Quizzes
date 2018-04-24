package com.bocian.quizzes.exceptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RestValidationErrorMessageFactoryTest {

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(exception.getBindingResult()).thenReturn(bindingResult);
    }

    @Test
    public void shouldGetCorrectMessageFromExceptionWithMultipleErrors() {
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(
                new ObjectError("obj1", "msg1"),
                new ObjectError("obj2", "msg2")));

        assertEquals("msg1. msg2.", RestValidationErrorMessageFactory.getDefaultMessage(exception));
    }
}