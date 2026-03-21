package com.grazac.springauthgrazac.product;

import org.springframework.security.core.Authentication;

public interface ProductService {
    ProductDtoResponse createProduct(ProductDto productDto);
    ProductDtoResponse createProductV2(ProductDto productDto, Authentication authentication);
}
