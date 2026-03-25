package com.grazac.springauthgrazac.customer;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String email;
}