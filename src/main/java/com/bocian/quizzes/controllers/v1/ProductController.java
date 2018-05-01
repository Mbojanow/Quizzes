package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.api.v1.model.ProductListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.services.api.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bocian.quizzes.controllers.v1.ProductController.PRODUCTS_BASE_URL;

@RestController
@RequestMapping(PRODUCTS_BASE_URL)
public class ProductController {

    public static final String PRODUCTS_BASE_URL = "/api/v1/products";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO getAllProducts() {
        return new ProductListDTO(productService.findAllProducts());
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductByName(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        return productService.findProductByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody final ProductDTO productDTO) throws InvalidRequestException {
        return productService.createProduct(productDTO);
    }

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@PathVariable("name") final String name,
                                    @Valid @RequestBody final ProductDTO productDTO) throws DbObjectNotFoundException {
        return productService.saveProduct(name, productDTO);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        productService.deleteProduct(name);
    }


}
