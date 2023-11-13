package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.service.AuthZService;
import com.dane.homework_help.dto.SubjectDTO;
import com.dane.homework_help.entity.Subject;
import com.dane.homework_help.repository.SubjectRepository;
import com.dane.homework_help.service.SubjectService;
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
