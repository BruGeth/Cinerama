package com.cinerama.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConfectioneryPurchaseRequest {
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private int quantity;
    }
}