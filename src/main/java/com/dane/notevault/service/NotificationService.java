package com.dane.notevault.service;

import com.dane.notevault.dto.NotificationDTO;
import com.dane.notevault.entity.Notification;
import com.dane.notevault.entity.enums.NotificationOrMessageStatus;
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
