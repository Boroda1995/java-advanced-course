package com.galdovich.java.course.service;

import com.galdovich.java.course.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    Customer save(Customer customer);

    Customer update(Long id, Customer customer);

    void delete(Long id);
}
