package com.dane.homework_help.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PostToSubject> subjects;

    @OneToMany(mappedBy = "post")
    private List<File> files;

    public UUID getUserId() {
        return user.getId();
    }

    public List<UUID> getSubjectIds() {
        return subjects == null ? new ArrayList<>() : subjects.stream().map(PostToSubject::getId).toList();
    }

    public void setSubjectIds(List<UUID> subjectsIds) {
        this.subjects = subjectsIds == null ? new ArrayList<>() : subjectsIds.stream()
                .map(id -> PostToSubject.builder().id(id).build())
                .toList();
    }

    public List<UUID> getFilesIds() {
        return files == null ? new ArrayList<>() : files.stream().map(File::getId).toList();
    }

    public void setFilesIds(List<UUID> filesIds) {
        this.files = filesIds == null ? new ArrayList<>() : filesIds.stream()
                .map(id -> File.builder().id(id).build())
                .toList();
    }
}
