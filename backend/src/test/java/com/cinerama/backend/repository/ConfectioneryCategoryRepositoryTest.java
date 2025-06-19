package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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
}