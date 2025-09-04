package org.example.namelesschamber.common.security;

public record CustomPrincipal(
        String subject,
        String role
) { }
