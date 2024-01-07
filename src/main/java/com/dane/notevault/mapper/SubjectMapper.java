package com.dane.notevault.mapper;

import com.dane.notevault.dto.SubjectDTO;
import com.dane.notevault.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SubjectMapper implements Function<Subject, SubjectDTO> {
    @Override
    public SubjectDTO apply(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription(),
                subject.getPostToSubjectIds());
    }
}
