package com.galdovich.security.service;

import com.galdovich.security.entity.LoginAttempt;
import com.galdovich.security.repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BruteForceProtectionService {

    private final LoginAttemptRepository loginAttemptRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 5;

    public void registerLoginFailure(String email) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setEmail(email);
        attempt.setSuccessful(false);
        loginAttemptRepository.save(attempt);
    }

    public boolean isBlocked(String email) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(BLOCK_DURATION_MINUTES);
        long failedAttempts = loginAttemptRepository.countRecentFailedAttempts(email, cutoff);
        return failedAttempts >= MAX_ATTEMPTS;
    }

    public void registerLoginSuccess(String email) {
        loginAttemptRepository.deleteAllByEmail(email);
    }

    public int getRemainingAttempts(String email) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(BLOCK_DURATION_MINUTES);
        long failedAttempts = loginAttemptRepository.countRecentFailedAttempts(email, cutoff);
        return (int) (MAX_ATTEMPTS - failedAttempts);
    }

    public List<String> getCurrentlyBlockedEmails() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(BLOCK_DURATION_MINUTES);

        return loginAttemptRepository.findRecentFailedAttemptsGroupedByEmail(cutoff).stream()
            .filter(emailAttempts -> emailAttempts.getAttemptCount() >= MAX_ATTEMPTS)
            .map(LoginAttemptRepository.EmailAttempts::getEmail)
            .collect(Collectors.toList());
    }

    public long getRemainingBlockTime(String email) {
        LocalDateTime lastFailedAttempt = loginAttemptRepository
            .findLastFailedAttemptTime(email)
            .orElse(LocalDateTime.now());

        LocalDateTime unblockTime = lastFailedAttempt.plusMinutes(BLOCK_DURATION_MINUTES);
        return Duration.between(LocalDateTime.now(), unblockTime).toMinutes();
    }
}
