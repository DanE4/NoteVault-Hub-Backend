package com.dane.notevault.service;

import com.dane.notevault.dto.ChatDTO;
import com.dane.notevault.dto.MessageDTO;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    ChatDTO createChat(ChatDTO chat);
    MessageDTO createMessage(MessageDTO message);
    MessageDTO updateMessage(MessageDTO message);
    MessageDTO getMessagesByChatId(Long id);
    MessageDTO deleteMessageById(Long id);
    ChatDTO updateChat(ChatDTO chat);
    ChatDTO getChatById(Long id);
    ChatDTO getChatByUserId(Long id);
    ChatDTO getChatByPostId(Long id);
    ChatDTO deleteChatById(Long id);
}
