package com.galdovich.java.course.repository;

import com.galdovich.java.course.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void saveAndFindByIdReturnsSavedCustomer() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer = customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findById(customer.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Customer");
    }

    @Test
    void findAllReturnsAllSavedCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer A");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Customer B");
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(2);
        assertThat(customers).extracting(Customer::getName).containsExactlyInAnyOrder("Customer A", "Customer B");
    }

    @Test
    void deleteRemovesCustomer() {
        Customer customer = new Customer();
        customer.setName("Delete Me");
        customer = customerRepository.save(customer);

        customerRepository.delete(customer);

        Optional<Customer> found = customerRepository.findById(customer.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    void existsByIdReturnsTrueWhenCustomerExists() {
        Customer customer = new Customer();
        customer.setName("Exists Test");
        customer = customerRepository.save(customer);

        boolean exists = customerRepository.existsById(customer.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdReturnsFalseWhenCustomerDoesNotExist() {
        boolean exists = customerRepository.existsById(999L);

        assertThat(exists).isFalse();
    }
}
