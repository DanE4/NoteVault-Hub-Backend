package com.dane.homework_help.repository;

import com.dane.homework_help.entity.PostToSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostToSubjectRepository extends JpaRepository<PostToSubject, UUID> {
    void deleteAllByPostId(UUID id);
}
