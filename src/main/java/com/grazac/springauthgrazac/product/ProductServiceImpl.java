package com.grazac.springauthgrazac.product;

import com.grazac.springauthgrazac.user.CurrentUserUtil;
import com.grazac.springauthgrazac.user.User;
import com.grazac.springauthgrazac.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, CurrentUserUtil currentUserUtil, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.currentUserUtil = currentUserUtil;
        this.userRepository = userRepository;
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

    // authentication
    @Override
    public ProductDtoResponse createProductV2(ProductDto productDto, Authentication authentication) {
        String username = authentication.getName();
        log.info("===============================================");
        log.info(username);
        log.info("===============================================");

        Optional<Product> productExists = productRepository.findProductByNameContainingIgnoreCase(productDto.getName());
        if(productExists.isPresent()) throw new RuntimeException("product already exist");


        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isEmpty()) throw new RuntimeException("user not found");
        Long userId = user.get().getId();
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

    @Override
    public List<ProductDtoResponse> getAll() {
        // LIST --->  [ ]
        // OBJECT ---> { }
        // List<Product> ---> [{}. {}. ....N{}]
        // method 1
//        List<Product> productEntity = productRepository.findAll(); // this will got to your database
//        return productEntity.stream().map(p -> ProductDtoResponse.builder()
//                .price(p.getPrice())
//                .category(p.getCategory())
//                .name(p.getName())
//                .build()).toList();

        //method 2
      return productRepository.findAll()
                .stream().map(p -> ProductDtoResponse.builder()
                .price(p.getPrice())
                .category(p.getCategory())
                .name(p.getName())
                .build()).toList();
    }
}
