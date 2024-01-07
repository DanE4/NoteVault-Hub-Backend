package com.dane.homework_help.service;

import com.dane.homework_help.dto.NotificationDTO;
import com.dane.homework_help.entity.Notification;
import com.dane.homework_help.entity.enums.NotificationOrMessageStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NotificationService {
    Notification createNotification(NotificationDTO notification, UUID userId);

    Notification getNotificationById(UUID id);

    List<Notification> getAllNotificationsForUser(UUID id);

    Notification updateNotificationStatus(UUID id, NotificationOrMessageStatus status);
}
