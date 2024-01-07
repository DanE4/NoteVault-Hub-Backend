package com.dane.notevault.mapper;

import com.dane.notevault.dto.ChatDTO;
import com.dane.notevault.entity.Chat;

public class ChatMapper {
    public static ChatDTO apply(Chat chat) {
        return new ChatDTO(chat.getId(), chat.getTitle(), chat.getChatToUserIds(), chat.getGroupToChatIds(), chat.getMessageIds(), chat.getFileIds());
    }
}
