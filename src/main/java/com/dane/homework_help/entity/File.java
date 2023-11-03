package com.dane.homework_help.entity;

import com.dane.homework_help.entity.enums.FileExtension;
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
public class File {
    //for now those are only photos and pdfs, pdfs should be sanitized before upload
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private FileExtension fileExtension;

    private String path;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
