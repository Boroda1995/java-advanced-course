package com.galdovich.security.repository;

import com.galdovich.security.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    @Query("SELECT COUNT(la) FROM LoginAttempt la WHERE la.email = :email AND la.attemptTime > :cutoff AND la.successful = false")
    long countRecentFailedAttempts(@Param("email") String email, @Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT la.email as email, COUNT(la) as attemptCount " +
        "FROM LoginAttempt la " +
        "WHERE la.attemptTime > :cutoff AND la.successful = false " +
        "GROUP BY la.email")
    List<EmailAttempts> findRecentFailedAttemptsGroupedByEmail(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT MAX(la.attemptTime) FROM LoginAttempt la " +
        "WHERE la.email = :email AND la.successful = false")
    Optional<LocalDateTime> findLastFailedAttemptTime(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM LoginAttempt la WHERE la.email = :email")
    void deleteAllByEmail(@Param("email") String email);

    void deleteByAttemptTimeBefore(LocalDateTime localDateTime);

    interface EmailAttempts {
        String getEmail();
        long getAttemptCount();
    }
}