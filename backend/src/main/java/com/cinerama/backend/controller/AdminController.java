package com.cinerama.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * Endpoint to access the admin dashboard.
     * Only users with the ADMIN role can access this endpoint.
     *
     * @return A welcome message for the admin dashboard.
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

}