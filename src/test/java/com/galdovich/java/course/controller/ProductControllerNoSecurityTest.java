package com.galdovich.java.course.controller;

import com.galdovich.java.course.entity.Product;
import com.galdovich.java.course.prometheus.CustomMetrics;
import com.galdovich.java.course.security.JwtUtil;
import com.galdovich.java.course.security.OAuth2LoginSuccessHandler;
import com.galdovich.java.course.security.SecurityConfig;
import com.galdovich.java.course.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class})
@TestPropertySource(properties = "spring.security.enabled=false")
class ProductControllerNoSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
    @MockBean
    private CustomMetrics customMetrics;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllProductsWithoutSecurity() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product One");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product Two");

        when(productService.findAll()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Product One"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Product Two"));

        verify(customMetrics, times(1)).incrementCustomCounter();
    }

    @Test
    void testGetProductByIdFound() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product One");

        when(productService.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Product One"));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/1"))
            .andExpect(status().isNotFound());
    }
}

