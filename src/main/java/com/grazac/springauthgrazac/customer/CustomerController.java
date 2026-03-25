package com.grazac.springauthgrazac.customer;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public CustomerResponse create(@RequestBody CustomerRequest request) {
        return service.createCustomer(request);
    }

    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable Long id) {
        return service.getCustomer(id);
    }

    @GetMapping
    public List<CustomerResponse> getAll() {
        return service.getAllCustomers();
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id,
                                   @RequestBody CustomerRequest request) {
        return service.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCustomer(id);
    }
}
