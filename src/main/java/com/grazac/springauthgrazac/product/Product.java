package com.grazac.springauthgrazac.product;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// c --> floats and double
// precisions
// floats -> 6 decimals digits i.e 6.7 => 6.700000
// double -> 6.7 dec*imals => 6.7000000000000000

@Table(name = "tbl_product")
@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private BigDecimal price;

    private Long createdBy; // relationship between the user POJO and the product POJO
}
