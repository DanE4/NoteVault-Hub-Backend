package com.dane.homework_help.service.impl;

import com.dane.homework_help.dto.ChatDTO;
import com.dane.homework_help.dto.MessageDTO;
import com.dane.homework_help.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    @Override
    public ChatDTO createChat(ChatDTO chat) {
        //this can be called
        return null;
    }

    @Override
    public MessageDTO createMessage(MessageDTO message) {
        return null;
    }

    @Override
    public MessageDTO updateMessage(MessageDTO message) {
        return null;
    }

    @Override
    public MessageDTO getMessagesByChatId(Long id) {
        return null;
    }

    @Override
    public MessageDTO deleteMessageById(Long id) {
        return null;
    }

    @Override
    public ChatDTO updateChat(ChatDTO chat) {
        return null;
    }

    @Override
    public ChatDTO getChatById(Long id) {
        return null;
    }

    @Override
    public ChatDTO getChatByUserId(Long id) {
        return null;
    }

    @Override
    public ChatDTO getChatByPostId(Long id) {
        return null;
    }

    @Override
    public ChatDTO deleteChatById(Long id) {
        return null;
    }
}
