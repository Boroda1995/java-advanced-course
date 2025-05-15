package com.galdovich.java.course.service.impl;

import com.galdovich.java.course.entity.Product;
import com.galdovich.java.course.repository.ProductRepository;
import com.galdovich.java.course.service.ProductService;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findAllReturnsAllProducts() {
        List<Product> productList = List.of(
            new Product(1L, "Product A", 10.0),
            new Product(2L, "Product B", 20.0)
        );

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
    }

    @Test
    void findByIdReturnsProductWhenFound() {
        Product product = new Product(1L, "Product A", 15.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product A", result.get().getName());
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void savePersistsAndReturnsProduct() {
        Product product = new Product(null, "New Product", 25.0);
        Product savedProduct = new Product(1L, "New Product", 25.0);

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.save(product);

        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
    }

    @Test
    void updateModifiesAndReturnsProductWhenFound() {
        Product existingProduct = new Product(1L, "Old Name", 20.0);
        Product productDetails = new Product(null, "Updated Name", 30.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.update(1L, productDetails);

        assertEquals("Updated Name", result.getName());
        assertEquals(30.0, result.getPrice());
    }

    @Test
    void updateThrowsExceptionWhenProductNotFound() {
        Product productDetails = new Product(null, "Updated Name", 30.0);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.update(1L, productDetails);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void deleteRemovesProductWhenFound() {
        Product existingProduct = new Product(1L, "Product A", 10.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        productService.delete(1L);

        verify(productRepository).delete(existingProduct);
    }

    @Test
    void deleteThrowsExceptionWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.delete(1L);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
