package com.galdovich.java.course.repository;

import com.galdovich.java.course.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
