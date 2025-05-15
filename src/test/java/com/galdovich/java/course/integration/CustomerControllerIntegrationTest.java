package com.galdovich.java.course.integration;

import com.galdovich.java.course.entity.Customer;
import com.galdovich.java.course.entity.Product;
import com.galdovich.java.course.repository.CustomerRepository;
import com.galdovich.java.course.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        Customer customer = new Customer();
        customer.setName("John Doe");
        customerRepository.save(customer);

        productRepository.deleteAll();
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(19.99);
        productRepository.save(product);
    }

    @Test
    void testGetAllCustomersWithOAuth2LoginShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/customers")
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                    .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetCustomerByIdWithOAuth2LoginShouldReturnOk() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        mockMvc.perform(get("/api/v1/customers/" + customer.getId())
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                    .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetCustomerByIdNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/customers/9999")
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                    .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isNotFound());
    }
}

