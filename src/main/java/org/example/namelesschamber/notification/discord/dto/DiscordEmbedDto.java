package org.example.namelesschamber.notification.discord.dto;

import java.util.List;

public record DiscordEmbedDto(
        String title,
        int color,
        List<Field> fields,
        Footer footer,
        String timestamp
) {
    public record Field(String name, String value, boolean inline) {}
    public record Footer(String text, String iconUrl) {}
}
