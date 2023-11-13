package com.dane.homework_help.service;

import com.dane.homework_help.dto.SubjectDTO;
import com.dane.homework_help.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {
    List<Subject> getAllSubjects();

    Subject getSubjectById();

    Subject getSubjectByName();

    Subject createSubject(SubjectDTO subjectDTO);

    Subject updateSubject(SubjectDTO subjectDTO);

    void deleteSubjectById();
}
