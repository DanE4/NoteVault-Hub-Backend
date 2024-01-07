package com.dane.notevault.dto;

import com.dane.notevault.entity.enums.NotificationOrMessageStatus;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record NotificationDTO(
        UUID id,
        String message,
        String type,
        String link,
        NotificationOrMessageStatus status,
        UUID userId

) implements Serializable {
}
