package com.bocian.quizzes.bootstrap;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;

public class InitialDataLoaderTest {

    private DataLoader loaderA = mock(DataLoader.class);
    private DataLoader loaderB = mock(DataLoader.class);
    private DataLoader loaderC = mock(DataLoader.class);
    private DataLoader loaderD = mock(DataLoader.class);
    private ContextRefreshedEvent contextRefreshedEvent = mock(ContextRefreshedEvent.class);

    @Test
    public void shouldCallSaveOnAllLoadersOnApplicationEvent() {
        final InitialDataLoader initialDataLoader = new InitialDataLoader(loaderA, loaderB, loaderC, loaderD);
        initialDataLoader.onApplicationEvent(contextRefreshedEvent);
        Mockito.verify(loaderA).save();
        Mockito.verify(loaderB).save();
        Mockito.verify(loaderC).save();
        Mockito.verify(loaderD).save();
    }

}