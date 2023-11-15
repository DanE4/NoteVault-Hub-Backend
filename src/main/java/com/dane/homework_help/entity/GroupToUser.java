package com.dane.homework_help.entity;

import com.dane.homework_help.entity.enums.GroupRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

}
