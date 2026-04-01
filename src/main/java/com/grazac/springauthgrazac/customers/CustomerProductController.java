package com.grazac.springauthgrazac.customers;

import com.grazac.springauthgrazac.product.ProductDtoResponse;
import com.grazac.springauthgrazac.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/products")
public class CustomerProductController {

    private final ProductService productService;

    public CustomerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDtoResponse>> getAllProducts(){
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }
}
