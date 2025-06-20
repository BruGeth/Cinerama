package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.repository.ConfectioneryCategoryRepository;
import com.cinerama.backend.service.ConfectioneryCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfectioneryCategoryServiceImpl implements ConfectioneryCategoryService {

    private final ConfectioneryCategoryRepository repository;

    @Override
    public List<ConfectioneryCategoryResponse> getAllCategories() {
        return repository.findAll().stream()
                .map(category -> new ConfectioneryCategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}