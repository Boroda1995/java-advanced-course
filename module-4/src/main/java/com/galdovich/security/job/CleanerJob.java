package com.galdovich.security.job;

import com.galdovich.security.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CleanerJob {

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupOldAttempts() {
        loginAttemptRepository.deleteByAttemptTimeBefore(
            LocalDateTime.now().minusDays(7));
    }
}
