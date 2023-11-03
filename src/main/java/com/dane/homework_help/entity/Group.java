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
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;

    //subjects

    @OneToMany(mappedBy = "group")
    private List<GroupToSubject> subjects;

    //group chat

    @OneToMany(mappedBy = "group")
    private List<GroupToChat> chats;

    //users

    @OneToMany(mappedBy = "group")
    private List<GroupToUser> users;
}
