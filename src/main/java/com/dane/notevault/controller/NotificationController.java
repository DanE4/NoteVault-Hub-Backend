package com.dane.notevault.controller;

import com.dane.notevault.auth.Response;
import com.dane.notevault.dto.NotificationDTO;
import com.dane.notevault.entity.Notification;
import com.dane.notevault.entity.enums.NotificationOrMessageStatus;
import com.dane.notevault.exception.RecordNotFoundException;
import com.dane.notevault.exception.UserNotFoundException;
import com.dane.notevault.mapper.NotificationMapper;
import com.dane.notevault.service.impl.NotificationServiceImpl;
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
    @PostMapping("/{userId}")
    public ResponseEntity<Response> createNotification(@PathVariable(value = "userId") UUID userId,
                                                       @RequestBody NotificationDTO notification) {
        try {
            var tokens = notificationMapper.apply(notificationService.createNotification(notification,
                    userId));
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(tokens)
                            .build());

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().response("User not found").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().response("Error").build());
        }
    }

    @Operation(
            description = "Get endpoint for notifications",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation =
                                            NotificationDTO.class)
                            )
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
    @GetMapping("/{notification_id}")
    public ResponseEntity<Response> getNotificationById(@PathVariable(value = "notification_id") UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(notificationMapper.apply(notificationService.getNotificationById(id)))
                            .build());

        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().response("Notification not found").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().response("Error").build());
        }
    }

    //TODO: This should be a list of notifications
    @Operation(
            description = "Get endpoint for notifications",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation =
                                            NotificationDTO.class)
                            )
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<Response> getAllNotificationsForUser(@PathVariable(value = "userId") UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(notificationService.getAllNotificationsForUser(id)
                                    .stream()
                                    .map(notificationMapper)
                                    .toList())
                            .build());

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().response("User not found").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().response("Error").build());
        }
    }

    @Operation(
            description = "Update endpoint for notifications",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation =
                                            NotificationDTO.class)
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
    @PatchMapping(value = "/{notification_id}/{status}")
    public ResponseEntity<Response> updateNotificationStatus(@PathVariable(value = "notification_id") UUID id,
                                                             @PathVariable(value = "status") NotificationOrMessageStatus status) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(notificationMapper.apply(notificationService.updateNotificationStatus(id, status)))
                            .build());

        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().response("Notification not found").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().response("Error").build());
        }
    }
}
