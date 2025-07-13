package com.cinerama.backend.service;

import com.cinerama.backend.dto.ConfectioneryPurchaseItemDTO;
import com.cinerama.backend.entity.ConfectioneryPurchase;

import java.util.List;

public interface ConfectioneryPurchaseService {
    ConfectioneryPurchase registerPurchase(List<ConfectioneryPurchaseItemDTO> items);
}
