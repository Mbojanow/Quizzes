package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.configuration.Profiles;
import com.bocian.quizzes.model.Product;
import com.bocian.quizzes.repositories.AnswerRepository;
import com.bocian.quizzes.repositories.ProductRepository;
import com.bocian.quizzes.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Profile(Profiles.DEVELOPMENT)
@Slf4j
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final List<DataLoader> loaders;

    public InitialDataLoader(@Qualifier("PRODUCT") final DataLoader productDataLoader,
                             @Qualifier("CPP") final DataLoader cppDataLoader,
                             @Qualifier("JAVA") final DataLoader javaDataLoader) {
        loaders = Arrays.asList(productDataLoader, cppDataLoader, javaDataLoader);
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Dev bootstrap started. Initializing database.");
        loaders.forEach(DataLoader::save);
    }

}
