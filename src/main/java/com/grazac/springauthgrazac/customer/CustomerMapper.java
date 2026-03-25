package com.grazac.springauthgrazac.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        if (request == null) return null;

        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }


    // destination                       source will be the parmas ----> cue
    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
}