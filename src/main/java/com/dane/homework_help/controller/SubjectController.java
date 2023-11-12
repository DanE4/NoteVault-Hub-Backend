package com.dane.homework_help.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subject")
public class SubjectController {
    @GetMapping
    public String getSubjects() {
        return "Hello World";
    }
}
