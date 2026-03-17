package com.grazac.springauthgrazac.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter @Builder
public class ProductDtoResponse {
    private  String name;
    private  String category;
    private BigDecimal price;
}
