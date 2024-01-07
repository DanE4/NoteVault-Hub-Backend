package com.dane.notevault.service;

import com.dane.notevault.dto.SubjectDTO;
import com.dane.notevault.entity.Subject;
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
