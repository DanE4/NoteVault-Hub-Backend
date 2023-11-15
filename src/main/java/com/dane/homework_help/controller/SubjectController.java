package com.dane.homework_help.controller;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.SubjectDTO;
import com.dane.homework_help.mapper.SubjectMapper;
import com.dane.homework_help.service.impl.SubjectServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Subject")
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectServiceImpl subjectService;
    private final SubjectMapper subjectMapper;

    @GetMapping
    public String getSubjects() {
        return "Hello World";
    }

    @PostMapping
    public ResponseEntity<Response> createSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(subjectMapper.apply(subjectService.createSubject(subjectDTO)))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
