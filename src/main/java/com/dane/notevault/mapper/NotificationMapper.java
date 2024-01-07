package com.dane.notevault.mapper;

import com.dane.notevault.dto.NotificationDTO;
import com.dane.notevault.entity.Notification;
import com.dane.notevault.entity.enums.NotificationOrMessageStatus;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NotificationMapper implements Function<Notification, NotificationDTO> {
    @Override
    public NotificationDTO apply(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .link(notification.getLink())
                .status(NotificationOrMessageStatus.valueOf(notification.getStatus().name()))
                .userId(notification.getUser().getId())
                .build();
    }
}
