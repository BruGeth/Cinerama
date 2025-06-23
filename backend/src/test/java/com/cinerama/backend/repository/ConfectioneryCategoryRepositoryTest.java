package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConfectioneryCategoryRepositoryTest {

    @Autowired
    private ConfectioneryCategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save and retrieve a category")
    void shouldSaveAndRetrieveCategory() {
        ConfectioneryCategory category = ConfectioneryCategory.builder()
                .name("Candy")
                .build();

        categoryRepository.save(category);

        List<ConfectioneryCategory> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("Candy");
    }

    @Test
    @DisplayName("Should update a category")
    void shouldUpdateCategory() {
        ConfectioneryCategory category = categoryRepository.save(
                ConfectioneryCategory.builder().name("Candy").build()
        );
        category.setName("Updated Candy");
        categoryRepository.save(category);

        Optional<ConfectioneryCategory> updated = categoryRepository.findById(category.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated Candy");
    }

    @Test
    @DisplayName("Should delete a category")
    void shouldDeleteCategory() {
        ConfectioneryCategory category = categoryRepository.save(
                ConfectioneryCategory.builder().name("Candy").build()
        );
        categoryRepository.delete(category);

        List<ConfectioneryCategory> categories = categoryRepository.findAll();
        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when category not found")
    void shouldReturnEmptyWhenNotFound() {
        Optional<ConfectioneryCategory> result = categoryRepository.findById(999L);
        assertThat(result).isNotPresent();
    }
}