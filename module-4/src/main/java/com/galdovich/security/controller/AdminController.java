package com.galdovich.security.controller;

import com.galdovich.security.dto.BlockedUserDto;
import com.galdovich.security.service.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final BruteForceProtectionService bruteForceService;

    @GetMapping("/blocked-users")
    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    public ResponseEntity<List<BlockedUserDto>> getBlockedUsers() {
        List<String> blockedEmails = bruteForceService.getCurrentlyBlockedEmails();

        List<BlockedUserDto> result = blockedEmails.stream()
            .map(email -> new BlockedUserDto(
                email,
                bruteForceService.getRemainingBlockTime(email)
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    public String adminInfo() {
        return "Admin Dashboard";
    }
}
