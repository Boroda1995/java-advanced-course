package com.galdovich.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    public String adminInfo() {
        return "Admin Dashboard";
    }
}
