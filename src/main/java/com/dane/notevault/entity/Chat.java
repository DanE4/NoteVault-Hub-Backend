package com.dane.notevault.entity;

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

    private String title;

    @OneToMany(mappedBy = "chat")
    private List<ChatToUser> users;

    @OneToMany(mappedBy = "chat")
    private List<GroupToChat> groups;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @OneToMany(mappedBy = "chat")
    private List<File> files;

    public List<UUID> getChatToUserIds() {
        return users.stream().map(ChatToUser::getId).toList();
    }

    public List<UUID> getGroupToChatIds() {
        return groups.stream().map(GroupToChat::getId).toList();
    }

    public List<UUID> getMessageIds() {
        return messages.stream().map(Message::getId).toList();
    }

    public List<UUID> getFileIds() {
        return files.stream().map(File::getId).toList();
    }
}
