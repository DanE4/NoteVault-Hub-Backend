package com.dane.homework_help.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "message", example = "You have a new post")
    private String message;
    @Schema(description = "type", example = "post")
    private String type;
    @Schema(description = "link", example = "http://localhost:8080/api/v1/posts/1")
    private String link;
    @Schema(hidden = true)
    private String isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
