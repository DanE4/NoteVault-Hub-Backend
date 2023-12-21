package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.ChatDTO;
import com.dane.homework_help.entity.Chat;

public class ChatMapper {
    public static ChatDTO apply(Chat chat) {
        return new ChatDTO(chat.getId(), chat.getTitle(), chat.getChatToUserIds(), chat.getGroupToChatIds(), chat.getMessageIds(), chat.getFileIds());
    }
}
