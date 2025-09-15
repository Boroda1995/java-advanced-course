package com.galdovich.security.controller;

import com.galdovich.security.entity.UserEntity;
import com.galdovich.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String blocked,
                            @RequestParam(required = false) Integer remaining,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", true);
            if (remaining != null) {
                model.addAttribute("remaining", remaining);
            }
        }

        if (blocked != null) {
            model.addAttribute("blocked", true);
        }

        if (logout != null) {
            model.addAttribute("logout", true);
        }

        return "login";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/info";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        try {
            authService.register(user);
            return ResponseEntity.ok("Registered");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
