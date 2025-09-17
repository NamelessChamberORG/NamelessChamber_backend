package org.example.namelesschamber.domain.auth.core;

public record CustomPrincipal(
        String subject,
        String role
) { }
