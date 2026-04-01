package com.grazac.springauthgrazac.product;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProductService {
    ProductDtoResponse createProduct(ProductDto productDto);
    ProductDtoResponse createProductV2(ProductDto productDto, Authentication authentication);
    List<ProductDtoResponse> getAll();
}
