package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.entity.ConfectioneryProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConfectioneryProductRepositoryTest {

    @Autowired
    private ConfectioneryProductRepository productRepository;

    @Autowired
    private ConfectioneryCategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save and retrieve a product")
    void shouldSaveAndRetrieveProduct() {
        ConfectioneryCategory category = categoryRepository.save(
                ConfectioneryCategory.builder().name("Candy").build()
        );
        ConfectioneryProduct product = ConfectioneryProduct.builder()
                .name("Lollipop")
                .description("Sweet candy")
                .price(1.5)
                .image("lollipop.png")
                .category(category)
                .build();

        productRepository.save(product);

        List<ConfectioneryProduct> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Lollipop");
    }

    @Test
    @DisplayName("Should find products by category id")
    void shouldFindByCategoryId() {
        ConfectioneryCategory category1 = categoryRepository.save(
                ConfectioneryCategory.builder().name("Candy").build()
        );
        ConfectioneryCategory category2 = categoryRepository.save(
                ConfectioneryCategory.builder().name("Chocolate").build()
        );

        productRepository.save(
                ConfectioneryProduct.builder()
                        .name("Lollipop")
                        .description("Sweet candy")
                        .price(1.5)
                        .image("lollipop.png")
                        .category(category1)
                        .build()
        );
        productRepository.save(
                ConfectioneryProduct.builder()
                        .name("Bar")
                        .description("Chocolate bar")
                        .price(2.0)
                        .image("bar.png")
                        .category(category2)
                        .build()
        );

        List<ConfectioneryProduct> candyProducts = productRepository.findByCategory_Id(category1.getId());
        assertThat(candyProducts).hasSize(1);
        assertThat(candyProducts.get(0).getName()).isEqualTo("Lollipop");
    }
}