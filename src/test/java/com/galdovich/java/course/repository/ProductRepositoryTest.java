package com.galdovich.java.course.repository;

import com.galdovich.java.course.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindByIdReturnsSavedProduct() {
        Product product = new Product(null, "Test Product", 29.99);
        product = productRepository.save(product);

        Optional<Product> found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Product");
    }

    @Test
    void findAllReturnsAllSavedProducts() {
        productRepository.save(new Product(null, "Product A", 10.0));
        productRepository.save(new Product(null, "Product B", 20.0));

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName).containsExactlyInAnyOrder("Product A", "Product B");
    }

    @Test
    void deleteRemovesProduct() {
        Product product = new Product(null, "Delete Me", 15.0);
        product = productRepository.save(product);

        productRepository.delete(product);

        Optional<Product> found = productRepository.findById(product.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    void existsByIdReturnsTrueWhenProductExists() {
        Product product = new Product(null, "Exists Test", 5.0);
        product = productRepository.save(product);

        boolean exists = productRepository.existsById(product.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdReturnsFalseWhenProductDoesNotExist() {
        boolean exists = productRepository.existsById(999L);

        assertThat(exists).isFalse();
    }
}
