package com.grazac.springauthgrazac.product;

import com.grazac.springauthgrazac.user.CurrentUserUtil;
import com.grazac.springauthgrazac.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CurrentUserUtil currentUserUtil;

    public ProductServiceImpl(ProductRepository productRepository, CurrentUserUtil currentUserUtil) {
        this.productRepository = productRepository;
        this.currentUserUtil = currentUserUtil;
    }

    @Override
    public ProductDtoResponse createProduct(ProductDto productDto) {
        User loggedInUser = currentUserUtil.getLoggedInUser();
        Long userId = loggedInUser.getId();

        Optional<Product> productExists = productRepository.findProductByNameContainingIgnoreCase(productDto.getName());
        if(productExists.isPresent()) throw new RuntimeException("product already exist");

        Product newProduct = Product.builder()
                .createdBy(userId)
                .price(productDto.getPrice())
                .name(productDto.getName())
                .category(productDto.getCategory())
                .build();
        Product savedProduct = productRepository.save(newProduct);
        return ProductDtoResponse.builder()
                .price(savedProduct.getPrice())
                .category(savedProduct.getCategory())
                .name(savedProduct.getName())
                .build();
    }
}
