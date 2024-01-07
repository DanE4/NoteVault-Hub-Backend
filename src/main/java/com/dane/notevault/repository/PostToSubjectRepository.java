package com.dane.notevault.repository;

import com.dane.notevault.entity.PostToSubject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostToSubjectRepository extends JpaRepository<PostToSubject, UUID> {
    @Transactional
    void deleteAllByPostId(UUID id);

    @Transactional
    void deleteByPostId(UUID id);
}
