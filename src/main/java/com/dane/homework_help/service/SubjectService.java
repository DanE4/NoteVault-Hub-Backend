package com.dane.homework_help.service;

import com.dane.homework_help.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {
    List<Subject> getAllSubjects();
}
