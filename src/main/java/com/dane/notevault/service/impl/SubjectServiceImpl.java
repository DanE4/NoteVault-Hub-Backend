package com.dane.notevault.service.impl;

import com.dane.notevault.auth.service.AuthZService;
import com.dane.notevault.dto.SubjectDTO;
import com.dane.notevault.entity.Subject;
import com.dane.notevault.repository.SubjectRepository;
import com.dane.notevault.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final AuthZService authZService;

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById() {
        return null;
    }

    @Override
    public Subject getSubjectByName() {
        return null;
    }

    @Override
    public Subject createSubject(SubjectDTO subjectDTO) {
        Subject subject = Subject.builder().name(subjectDTO.name()).description(subjectDTO.description()).build();
        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(SubjectDTO subjectDTO) {
        return null;
    }

    @Override
    public void deleteSubjectById() {

    }
}
