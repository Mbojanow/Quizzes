package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.services.api.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).
                setControllerAdvice(RestExceptionHandler.class, RestValidationExceptionHandler.class).build();
    }

    @Test
    public void shouldGetOkWhenGettingProducts() throws Exception {
        final int page = 0;
        final int size = 50;
        when(productService.getProducts(page, size)).thenReturn(Arrays.asList(
                new ProductDTO("prod1", "desc1", "none"),
                new ProductDTO("prod2", "desc2", "none")
        ));

        mockMvc.perform(get(ProductController.PRODUCTS_BASE_URL + PageRequestParamsFactory.get(page, size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)));
    }

    @Test
    public void shouldGetOkWhenGettingProductsAndNotSpecifyingPageAndSize() throws Exception {
        when(productService.getProducts(0, 50)).thenReturn(Arrays.asList(
                new ProductDTO("prod1", "desc1", "none"),
                new ProductDTO("prod2", "desc2", "none")
        ));

        mockMvc.perform(get(ProductController.PRODUCTS_BASE_URL + PageRequestParamsFactory.getWithDefaults())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)));
    }

    @Test
    public void shouldGetBadRequestWhenPageIsNotANumberWhenGettingProducts() throws Exception {
        mockMvc.perform(get(ProductController.PRODUCTS_BASE_URL + "?page=NOTANUMBER&size=")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message",
                        containsString("Failed to convert value to number")));
    }

    @Test
    public void shouldGetBadRequestWhenSizeIsNotANumberWhenGettingProducts() throws Exception {
        mockMvc.perform(get(ProductController.PRODUCTS_BASE_URL + "?page=&size=QWERTY")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message",
                        containsString("Failed to convert value to number")));
    }

    @Test
    public void shouldGetOkWhenGettingProductByName() throws Exception {
        final String name = "someName";
        final String desc = "someDesc";
        final String url = "someUrl";
        when(productService.findProductByName(any())).thenReturn(new ProductDTO(name, desc, url));
        mockMvc.perform(get(ProductController.PRODUCTS_BASE_URL + "/" + name)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(name)))
                .andExpect(jsonPath("$.description", Matchers.is(desc)))
                .andExpect(jsonPath("$.product_url", Matchers.is(url)));
    }

    @Test
    public void shouldGetCreatedWhenCreatingValidProduct() throws Exception {
        final String name = "MyNameIsMichal";
        final String desc = "IAmAJavaDeveloper";
        final String url = "AndThisIsMyUrl";
        final ProductDTO productDTO = new ProductDTO(name, desc, url);
        when(productService.createProduct(productDTO)).thenReturn(productDTO);
        mockMvc.perform(post(ProductController.PRODUCTS_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(name)))
                .andExpect(jsonPath("$.description", Matchers.is(desc)))
                .andExpect(jsonPath("$.product_url", Matchers.is(url)));
    }

    @Test
    public void shouldGetBadRequestWhenCreatingInvalidProduct() throws Exception {
        mockMvc.perform(post(ProductController.PRODUCTS_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new ProductDTO("", "", null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message",
                        containsString("Product name needs to be at least 2 characters long")))
                .andExpect(jsonPath("$.error_message",
                        containsString("Product description too short")))
                .andExpect(jsonPath("$.error_message",
                        containsString("name and description have to be different")));
    }

    @Test
    public void shouldGetOkWhenUpdatingValidProduct() throws Exception {
        final String name = "asd";
        final String desc = "qwertyqwea";
        final String url = "rty";
        final ProductDTO productDTO = new ProductDTO(name, desc, url);
        when(productService.saveProduct(name, productDTO)).thenReturn(productDTO);
        mockMvc.perform(put(ProductController.PRODUCTS_BASE_URL + "/" + name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(name)))
                .andExpect(jsonPath("$.description", Matchers.is(desc)))
                .andExpect(jsonPath("$.product_url", Matchers.is(url)));
    }

    @Test
    public void shouldGetBadRequestWhenUpdatingValidProduct() throws Exception {
        mockMvc.perform(put(ProductController.PRODUCTS_BASE_URL + "/qwerty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new ProductDTO("", "", null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_message",
                        containsString("Product name needs to be at least 2 characters long")))
                .andExpect(jsonPath("$.error_message",
                        containsString("Product description too short")))
                .andExpect(jsonPath("$.error_message",
                        containsString("name and description have to be different")));
    }

    @Test
    public void shouldGetNoContentWhenDeletingProduct() throws Exception {
        mockMvc.perform(delete(ProductController.PRODUCTS_BASE_URL + "/someProd"))
                .andExpect(status().isNoContent());
    }
}