package com.galdovich.java.course.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class ProfileController {

    @GetMapping("/me")
    public Map<String, Object> profile(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        result.put("principal", authentication.getPrincipal());
        result.put("authorities", authentication.getAuthorities());
        return result;
    }
}
