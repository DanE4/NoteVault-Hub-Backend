package com.dane.notevault.service.impl;

import com.dane.notevault.auth.service.AuthZService;
import com.dane.notevault.dto.NotificationDTO;
import com.dane.notevault.entity.Notification;
import com.dane.notevault.entity.User;
import com.dane.notevault.entity.enums.NotificationOrMessageStatus;
import com.dane.notevault.exception.RecordNotFoundException;
import com.dane.notevault.exception.UserNotFoundException;
import com.dane.notevault.mapper.NotificationMapper;
import com.dane.notevault.repository.NotificationRepository;
import com.dane.notevault.repository.UserRepository;
import com.dane.notevault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AuthZService authZService;
    private final NotificationMapper notificationMapper;


    @Override
    public Notification createNotification(NotificationDTO notificationData, UUID userId) {
        log.warn(notificationData.toString());
        log.info("Creating notification for user with id {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Notification savedNotification = Notification.builder()
                    .message(notificationData.message())
                    .type(notificationData.type())
                    .link(notificationData.link())
                    .user(user.get())
                    .build();
            user.get().getNotifications().add(savedNotification);
            userRepository.save(user.get());
            notificationRepository.save(savedNotification);
            return savedNotification;
        } else {
            log.error("User with id {} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
    }


    @Override
    @Cacheable(value = "notifications", key = "#id")
    public Notification getNotificationById(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Notification with id " + id + " not found"));
        authZService.CheckIfAuthorized(notification.getUser().getId());
        return notification;
    }


    @Override
    public List<Notification> getAllNotificationsForUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        authZService.CheckIfAuthorized(user.getId());
        return notificationRepository.findAllById(user.getNotifications().stream().map(Notification::getId).toList());
    }

    @Override
    public Notification updateNotificationStatus(UUID id, NotificationOrMessageStatus status) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Notification with id " + id + " not found"));
        authZService.CheckIfAuthorized(notification.getUser().getId());
        notification.setStatus(status);
        notificationRepository.save(notification);
        return notification;
    }
}
