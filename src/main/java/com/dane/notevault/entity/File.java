package com.dane.notevault.entity;

import com.dane.notevault.entity.enums.FileExtension;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "files")
public class File {
    //for now those are only photos and pdfs, pdfs should be sanitized before upload
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_extension")
    @Pattern(regexp = "JPG|PNG|GIF|PDF")
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
