package com.galdovich.security.security;

import com.galdovich.security.service.BruteForceProtectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private final BruteForceProtectionService bruteForceService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String email = request.getParameter("username");
        String redirectUrl = "/login?error";

        if (email != null) {
            bruteForceService.registerLoginFailure(email);
            int remainingAttempts = bruteForceService.getRemainingAttempts(email);

            if (remainingAttempts <= 0) {
                redirectUrl = "/login?blocked";
            } else {
                redirectUrl += "&remaining=" + remainingAttempts;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
