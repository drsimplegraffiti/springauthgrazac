package com.grazac.springauthgrazac.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Optional<Customer> customerExist = repository.findCustomerByEmail(request.getEmail());
        if(customerExist.isPresent()) throw new RuntimeException("customer already exist");
        // convert dto(request) to entity (db instance)
        Customer customer = mapper.toEntity(request);
        Customer saved = repository.save(customer);
        // convert entity to dto
        return mapper.toResponse(saved);
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse) // ✅ method reference updated
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        return mapper.toResponse(repository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}