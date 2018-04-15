package com.bocian.quizzes.bootstrap;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;

public class InitialDataLoaderTest {

    private DataLoader loaderA = mock(DataLoader.class);
    private DataLoader loaderB = mock(DataLoader.class);
    private DataLoader loaderC = mock(DataLoader.class);
    private ContextRefreshedEvent contextRefreshedEvent = mock(ContextRefreshedEvent.class);

    private InitialDataLoader initialDataLoader;

    @Test
    public void shouldCallSaveOnAllLoadersOnApplicationEvent() {
        initialDataLoader = new InitialDataLoader(loaderA, loaderB, loaderC);
        initialDataLoader.onApplicationEvent(contextRefreshedEvent);
        Mockito.verify(loaderA).save();
        Mockito.verify(loaderB).save();
        Mockito.verify(loaderC).save();
    }

}