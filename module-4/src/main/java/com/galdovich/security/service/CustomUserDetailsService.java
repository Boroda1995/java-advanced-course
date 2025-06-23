package com.galdovich.security.service;

import com.galdovich.security.entity.UserEntity;
import com.galdovich.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BruteForceProtectionService bruteForceService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (bruteForceService.isBlocked(email)) {
            throw new LockedException("Account temporarily locked due to multiple failed login attempts. Please try again later.");
        }

        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }

}
