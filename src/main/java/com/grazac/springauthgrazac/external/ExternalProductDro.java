package com.grazac.springauthgrazac.external;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@Getter  //? :token req.body
public class ExternalProductDro {
        private  String name;
        private  String category;
        private  String token;
        @Positive(message = "cannot be negative")
        private BigDecimal price;

}
