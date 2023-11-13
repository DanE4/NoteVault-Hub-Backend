package com.dane.homework_help.service;

import com.dane.homework_help.entity.Notification;
import com.dane.homework_help.entity.enums.NotificationOrMessageStatus;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification createNotification(Notification notification);

    Notification getNotificationById(UUID id);

    List<Notification> getAllNotificationsForUser(UUID id);

    Notification updateNotificationStatus(NotificationOrMessageStatus status);
}
