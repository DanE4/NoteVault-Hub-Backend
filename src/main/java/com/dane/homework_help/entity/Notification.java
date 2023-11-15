package com.dane.homework_help.entity;

import com.dane.homework_help.entity.enums.NotificationOrMessageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "message", example = "You have a new post")
    @Size(min = 2, max = 30)
    @NotEmpty
    private String message;

    @Schema(description = "type", example = "post")
    @Size(min = 2, max = 30)
    private String type;

    @NotEmpty
    @Schema(description = "link", example = "http://localhost:8080/api/v1/posts/1")
    private String link;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private NotificationOrMessageStatus status = NotificationOrMessageStatus.UNREAD;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "user_id", example = "14a8d1b6-6adf-4c75-b985-7d91f5483c07")
    private User user;
}
