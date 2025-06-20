package com.cinerama.backend.service;

import com.cinerama.backend.dto.ConfectioneryProductResponse;
import java.util.List;

public interface ConfectioneryProductService {
    List<ConfectioneryProductResponse> getAllProducts();
}