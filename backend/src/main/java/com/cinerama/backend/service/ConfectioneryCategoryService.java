package com.cinerama.backend.service;

import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import java.util.List;

public interface ConfectioneryCategoryService {
    List<ConfectioneryCategoryResponse> getAllCategories();
}