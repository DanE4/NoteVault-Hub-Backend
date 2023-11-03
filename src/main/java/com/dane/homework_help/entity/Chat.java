package com.dane.homework_help.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "chat")
    private List<ChatToUser> users;

    @OneToMany(mappedBy = "chat")
    private List<GroupToChat> groups;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @OneToMany(mappedBy = "chat")
    private List<File> files;
}
