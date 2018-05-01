package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.api.v1.model.ProductDTO;
import com.bocian.quizzes.api.v1.model.ProductListDTO;
import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.services.api.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bocian.quizzes.controllers.v1.ProductController.PRODUCTS_BASE_URL;

@Api(tags = {"Products"})
@RestController
@RequestMapping(PRODUCTS_BASE_URL)
public class ProductController {

    public static final String PRODUCTS_BASE_URL = "/api/v1/products";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Get a page of products with provided size", notes = "page and size are mandatory")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = {"page", "size"})
    public ProductListDTO getProduct(@RequestParam(value = "page", defaultValue = "0") final int page,
                                     @RequestParam(value = "size", defaultValue = "50") final int size) {
        return new ProductListDTO(productService.getProducts(page, size));
    }

    @ApiOperation("Get product by name")
    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductByName(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        return productService.findProductByName(name);
    }

    @ApiOperation(value = "Create a product", notes = "url in body is ignored")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody final ProductDTO productDTO) throws InvalidRequestException {
        return productService.createProduct(productDTO);
    }

    @ApiOperation("Update an existing product")
    @PutMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@PathVariable("name") final String name,
                                    @Valid @RequestBody final ProductDTO productDTO) throws DbObjectNotFoundException {
        return productService.saveProduct(name, productDTO);
    }

    @ApiOperation("Delete an existing product")
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("name") final String name) throws DbObjectNotFoundException {
        productService.deleteProduct(name);
    }


}
