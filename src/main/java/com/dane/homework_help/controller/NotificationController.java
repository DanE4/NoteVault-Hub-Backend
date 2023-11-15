package com.dane.homework_help.controller;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.NotificationDTO;
import com.dane.homework_help.entity.Notification;
import com.dane.homework_help.mapper.NotificationMapper;
import com.dane.homework_help.service.impl.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Notification")
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationServiceImpl notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(
            description = "Creating notifications",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Notification.class)
                            )

                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/notification/{user_id}")
    public ResponseEntity<Response> createNotification(@PathVariable(value = "user_id") UUID userId,
                                                       @RequestBody NotificationDTO notification) {
        try {
            var asd = notificationMapper.apply(notificationService.createNotification(notification,
                    userId));
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(asd)
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().data(e.getMessage()).build());
        }
    }

    @GetMapping("/notification/{notification_id}")
    public ResponseEntity<Response> getNotificationById(@PathVariable(value = "notification_id") UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(notificationMapper.apply(notificationService.getNotificationById(id)))
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/notification/user/{user_id}")
    public ResponseEntity<Response> getAllNotificationsForUser(@PathVariable(value = "user_id") UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(notificationService.getAllNotificationsForUser(id)
                                    .stream()
                                    .map(notificationMapper)
                                    .toList())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
