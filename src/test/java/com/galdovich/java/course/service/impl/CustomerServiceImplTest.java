package com.galdovich.java.course.service.impl;

import com.galdovich.java.course.entity.Customer;
import com.galdovich.java.course.entity.Product;
import com.galdovich.java.course.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void findAllReturnsAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Alice");
        customer1.setProducts(List.of());

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Bob");
        customer2.setProducts(List.of());

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> result = customerService.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void findByIdReturnsCustomerWhenFound() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setProducts(List.of());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void savePersistsAndReturnsCustomer() {
        Customer customer = new Customer();
        customer.setName("Charlie");
        customer.setProducts(List.of());

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName("Charlie");
        savedCustomer.setProducts(List.of());

        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        Customer result = customerService.save(customer);

        assertEquals(1L, result.getId());
        assertEquals("Charlie", result.getName());
    }

    @Test
    void updateModifiesAndReturnsCustomerWhenFound() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Old Name");
        existingCustomer.setProducts(List.of());

        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(10.0);
        List<Product> products = List.of(product);

        Customer customerDetails = new Customer();
        customerDetails.setName("Updated Name");
        customerDetails.setProducts(products);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.update(1L, customerDetails);

        assertEquals("Updated Name", result.getName());
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void updateThrowsExceptionWhenCustomerNotFound() {
        Customer customerDetails = new Customer();
        customerDetails.setName("Updated Name");
        customerDetails.setProducts(List.of());

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.update(1L, customerDetails);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void deleteRemovesCustomerWhenFound() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Alice");
        existingCustomer.setProducts(List.of());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        customerService.delete(1L);

        verify(customerRepository).delete(existingCustomer);
    }

    @Test
    void deleteThrowsExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.delete(1L);
        });

        assertEquals("Customer not found", exception.getMessage());
    }
}
