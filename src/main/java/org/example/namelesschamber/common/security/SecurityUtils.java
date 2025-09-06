package org.example.namelesschamber.common.security;

import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getCurrentSubject() {
        return getCustomPrincipal()
                .map(CustomPrincipal::subject)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
    }

    private static Optional<CustomPrincipal> getCustomPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(CustomPrincipal.class::isInstance)
                .map(CustomPrincipal.class::cast);
    }
}
