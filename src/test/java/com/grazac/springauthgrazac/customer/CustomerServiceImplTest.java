package com.grazac.springauthgrazac.customer;

//| Type             | What is real?                       | What is fake?            |
//        | ---------------- | ----------------------------------- | ------------------------ |
//        | Unit Test        | Only the class under test           | Everything else (mocked) |
//        | Integration Test | Multiple layers (DB, repo, service) | Nothing or very little   |

//Unit Test -> Service → (mock repo)
//        ✔ fast
//        ✔ isolated
//        ✔ no DB

//Integration Test
//Service → Repository → Database
//✔ real behavior
//        ✔ tests wiring/config
//        ❌ slower
//@SpringBootTest
//@Autowired
//CustomerRepository repository;
//👉 Loads Spring context + real DB connection
//types of integration test:
//@DataJpaTest tests only repository layer
//@SpringBootTest Full integration

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //using mockito to run the test
@DisplayName("Todo Service Impl Unit Test")
class CustomerServiceImplTest {

    // we dont want to directly interact with database, we need to mock them
    @Mock
    private CustomerRepository customerRepository; //  mock DB repository
    @Mock
    private CustomerMapper customerMapper; // mock mapper


    @InjectMocks // inject mocks will inject whatever you have as mocks up
    private CustomerServiceImpl customerService;
    // the class we want to test,but the CustomerServiceImpl needs the  private final CustomerRepository repository;
    // class under test or service under test

    // reusable test data (shared baseline objects)
    private CustomerRequest customerRequest;
    private Customer customer;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        // initialize common objects used across tests
        this.customerRequest = new CustomerRequest();
        customerRequest.setName("John");
        customerRequest.setEmail("john@example.com");

        this.customer = Customer.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();

        this.customerResponse = CustomerResponse.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();
    }

    @AfterEach
    void tearDown() {
        // optional cleanup (usually not needed in unit tests)
    }


    @Nested // grouping unit test together
    @DisplayName("create customer test")
    class CreateCustomerTest{

        @Test
        @DisplayName("should create a customer successfully when valid request is passed")
        void shouldCreateCustomerSuccessfully(){
            // request → mapper → entity → repository → saved entity → mapper → response
            // Given: setup (data + mocks) / prepare
//            CustomerRequest request = new CustomerRequest();
//            request.setName("John");
//            request.setEmail("john@example.com");
//
//            Customer mappedCustomer = Customer.builder()
//                    .name("John")
//                    .email("john@example.com")
//                    .build();

//            Customer savedCustomer = Customer.builder()
//                    .id(1L)
//                    .name("John")
//                    .email("john@example.com")
//                    .build();
//
//            CustomerResponse expectedResponse = CustomerResponse.builder()
//                    .id(1L)
//                    .name("John")
//                    .email("john@example.com")
//                    .build();

            // Given: setup (data + mocks) / prepare
//            Customer mappedCustomer = Customer.builder()
//                    .name(customerRequest.getName())
//                    .email(customerRequest.getEmail())
//                    .build(); // entity before saving (no ID yet)

            // mock mapper behavior
            when(customerMapper.toEntity(customerRequest)).thenReturn(customer);

            // mock repository behavior
            when(customerRepository.save(customer)).thenReturn(customer);

            // mock mapper response conversion
            when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

            // when: call the method you’re testing / act
            CustomerResponse actualResponse = customerService.createCustomer(customerRequest);


            // then: assert results + verify interactions / verify
            assertNotNull(actualResponse);
            assertEquals(1L, actualResponse.getId());
            assertEquals("John", actualResponse.getName());
            assertEquals("john@example.com", actualResponse.getEmail());

            // verify interactions (VERY IMPORTANT 🔥)
            verify(customerMapper, times(1)).toEntity(customerRequest);
            verify(customerRepository, times(1)).save(customer);
            verify(customerMapper, times(1)).toResponse(customer);

        }

        @Test
        @DisplayName("should throw exception when customer already exists")
        void shouldThrowExceptionWhenCustomerAlreadyExists() {

            // Given: a customer with the same email already exists
            when(customerRepository.findCustomerByEmail(customerRequest.getEmail()))
                    .thenReturn(Optional.of(customer)); // mock existing customer

            // When & Then: expect exception
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    customerService.createCustomer(customerRequest)
            );

            assertEquals("customer already exist", exception.getMessage());

            // Verify: mapper and save are NOT called
            verify(customerMapper, never()).toEntity(any());
            verify(customerRepository, never()).save(any());
            verify(customerMapper, never()).toResponse(any());
        }

    }



    // ===========================
    // GET CUSTOMER TESTS
    // ===========================
    @Nested
    @DisplayName("Get Customer Tests")
    class GetCustomerTests {

        @Test
        @DisplayName("should return customer when valid id is provided")
        void shouldReturnCustomerWhenIdExists() {
            // mock repository findById
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            // mock mapper toResponse
            when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

            // call service method
            CustomerResponse response = customerService.getCustomer(1L);

            // assertions
            assertNotNull(response);
            assertEquals(1L, response.getId());

            // verify interactions
            verify(customerRepository).findById(1L);
            verify(customerMapper).toResponse(customer);
        }

        @Test
        @DisplayName("should throw exception when customer id does not exist")
        void shouldThrowExceptionWhenIdNotExists() {
            // mock repository to return empty
            when(customerRepository.findById(2L)).thenReturn(Optional.empty());

            // assert exception
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    customerService.getCustomer(2L)
            );
            assertEquals("Customer not found", exception.getMessage());

            // verify mapper is never called
            verify(customerMapper, never()).toResponse(any());
        }
    }

    // ===========================
    // GET ALL CUSTOMERS TEST
    // ===========================
    @Nested
    @DisplayName("Get All Customers Tests")
    class GetAllCustomersTests {

        @Test
        @DisplayName("should return list of all customers")
        void shouldReturnAllCustomers() {
            // mock repository findAll
            when(customerRepository.findAll()).thenReturn(List.of(customer));
            // mock mapper toResponse
            when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

            // call service method
            List<CustomerResponse> responses = customerService.getAllCustomers();

            // assertions
            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertEquals("John", responses.get(0).getName());

            // verify interactions
            verify(customerRepository).findAll();
            verify(customerMapper).toResponse(customer);
        }

        @Test
        @DisplayName("should return empty list when no customers exist")
        void shouldReturnEmptyListWhenNoCustomers() {
            // mock repository findAll to return empty list
            when(customerRepository.findAll()).thenReturn(List.of());

            // call service method
            List<CustomerResponse> responses = customerService.getAllCustomers();

            // assertions
            assertNotNull(responses); // list should not be null
            assertTrue(responses.isEmpty()); // list should be empty

            // verify repository method called
            verify(customerRepository).findAll();
            // no mapping should occur since list is empty
            verify(customerMapper, never()).toResponse(any());
        }
    }

    // ===========================
    // UPDATE CUSTOMER TESTS
    // ===========================
    @Nested
    @DisplayName("Update Customer Tests")
    class UpdateCustomerTests {

        @Test
        @DisplayName("should update customer successfully when valid id and request are provided")
        void shouldUpdateCustomerSuccessfully() {
            // mock repository findById
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

            // mock repository save (after update)
            when(customerRepository.save(customer)).thenReturn(customer);

            // mock mapper toResponse
            when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

            // call service method
            CustomerResponse response = customerService.updateCustomer(1L, customerRequest);

            // assertions
            assertNotNull(response);
            assertEquals("John", response.getName());

            // verify interactions
            verify(customerRepository).findById(1L);
            verify(customerRepository).save(customer);
            verify(customerMapper).toResponse(customer);
        }

        @Test
        @DisplayName("should throw exception when updating non-existing customer")
        void shouldThrowExceptionWhenUpdatingNonExistingCustomer() {
            // mock repository findById to return empty
            when(customerRepository.findById(2L)).thenReturn(Optional.empty());

            // assert exception
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    customerService.updateCustomer(2L, customerRequest)
            );
            assertEquals("Customer not found", exception.getMessage());

            // verify save and mapper are never called
            verify(customerRepository, never()).save(any());
            verify(customerMapper, never()).toResponse(any());
        }
    }

    // ===========================
    // DELETE CUSTOMER TESTS
    // ===========================
    @Nested
    @DisplayName("Delete Customer Tests")
    class DeleteCustomerTests {

        @Test
        @DisplayName("should delete customer successfully when valid id is provided")
        void shouldDeleteCustomerSuccessfully() {
            // call service method
            customerService.deleteCustomer(1L);

            // verify repository deleteById called
            verify(customerRepository).deleteById(1L);
        }
    }





}