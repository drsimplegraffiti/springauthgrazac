package com.grazac.springauthgrazac.external;


import com.grazac.springauthgrazac.product.ProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external") //external/**
public class ExternalController {

    private final ExternalService externalService;

    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }

    // external/exposed/products
    @GetMapping("/exposed/products")
    public ResponseEntity<?> products(){
        return new ResponseEntity<>(externalService.getProductList(), HttpStatus.OK);
    }

    @PostMapping("/create/exposed/products") //bad
    public ResponseEntity<?> createProduct(@RequestBody @Valid ExternalProductDro productDto){
        return new ResponseEntity<>(externalService.createProduct(productDto), HttpStatus.OK);
    }
}
