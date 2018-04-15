package com.bocian.quizzes.bootstrap;

import com.bocian.quizzes.repositories.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialProductDataLoaderTest {

    @Autowired
    @Qualifier("PRODUCT")
    private DataLoader dataLoader;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldCreateSomeProducts() {
        dataLoader.save();
        assertThat(productRepository.count(), greaterThan(0L));
    }
}