package com.galdovich.java.course.controller;

import com.galdovich.java.course.entity.Product;
import com.galdovich.java.course.prometheus.CustomMetrics;
import com.galdovich.java.course.security.JwtRequestFilter;
import com.galdovich.java.course.security.JwtUtil;
import com.galdovich.java.course.security.OAuth2LoginSuccessHandler;
import com.galdovich.java.course.security.SecurityConfig;
import com.galdovich.java.course.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
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
@Import({SecurityConfig.class, JwtRequestFilter.class})
class ProductControllerTest {

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
    @WithMockUser(roles = "ADMIN")
    void testGetAllProductsWithAdminRole() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product One");

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Product One"));

        verify(customMetrics, times(1)).incrementCustomCounter();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllProductsAccessDeniedForUserRole() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllProductsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
}
