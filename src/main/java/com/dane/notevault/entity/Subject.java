package com.dane.notevault.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "subject")
    private List<PostToSubject> postToSubjects;

    public List<UUID> getPostToSubjectIds() {
        return postToSubjects == null ? new ArrayList<>() : postToSubjects.stream()
                .map(PostToSubject::getId)
                .collect(Collectors.toList());
    }

    public void setPostToSubjectIds(List<UUID> postToSubjectIds) {
        this.postToSubjects = postToSubjectIds == null ? new ArrayList<>() : postToSubjectIds.stream()
                .map(id -> PostToSubject.builder().id(id).build())
                .collect(Collectors.toList());
    }
}
