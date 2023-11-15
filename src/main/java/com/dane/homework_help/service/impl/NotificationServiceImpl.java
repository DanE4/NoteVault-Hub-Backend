package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.service.AuthZService;
import com.dane.homework_help.dto.NotificationDTO;
import com.dane.homework_help.entity.Notification;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.NotificationOrMessageStatus;
import com.dane.homework_help.exception.RecordNotFoundException;
import com.dane.homework_help.exception.UserNotFoundException;
import com.dane.homework_help.mapper.NotificationMapper;
import com.dane.homework_help.repository.NotificationRepository;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.NotificationService;
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
    public Notification updateNotificationStatus(NotificationOrMessageStatus status) {
        return null;
    }
}
