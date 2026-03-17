package com.grazac.springauthgrazac.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
@AllArgsConstructor
@Getter
public class ProductDto {
    private  String name;
    private  String category;
    private  BigDecimal price;
}