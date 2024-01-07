package com.dane.notevault.controller;

import com.dane.notevault.auth.Response;
import com.dane.notevault.dto.ChatDTO;
import com.dane.notevault.dto.MessageDTO;
import com.dane.notevault.exception.UnauthorizedException;
import com.dane.notevault.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Chat")
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    //TODO: implement all swagger docs for the endpoints

    //this is for bot the chats and messages
    /*
        ChatDTO createChat(ChatDTO chat);
        ChatDTO updateChat(ChatDTO chat);
        ChatDTO getChatById(Long id);
        ChatDTO getChatByUserId(Long id);
        ChatDTO getChatByPostId(Long id);
        ChatDTO deleteChatById(Long id);
        MessageDTO createMessage(MessageDTO message);
        MessageDTO updateMessage(MessageDTO message);
        MessageDTO getMessagesByChatId(Long id);
        MessageDTO deleteMessageById(Long id);
     */
    @Operation(summary = "Creates a chat", description = "Creating chat", tags = {"Chat"})
    @PostMapping("/")
    public ResponseEntity<Response> createChat(@RequestBody ChatDTO chatData) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(chatService.createChat(chatData)).build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        }
    }

    @Operation(summary = "Creates a message", description = "Creating message", tags = {"Chat"})
    @PostMapping("/message")
    public ResponseEntity<Response> createMessage(@RequestBody MessageDTO messageData) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(chatService.createMessage(messageData)).build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        }
    }

}
