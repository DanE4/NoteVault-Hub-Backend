package com.dane.notevault.controller;

import com.dane.notevault.auth.Response;
import com.dane.notevault.dto.SubjectDTO;
import com.dane.notevault.mapper.SubjectMapper;
import com.dane.notevault.service.impl.SubjectServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/")
    public String getSubjects() {
        return "all subjects";
    }

    @Operation(
            description = "Creating subjects",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SubjectDTO.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/")
    public ResponseEntity<Response> createSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder()
                            .data(subjectMapper.apply(subjectService.createSubject(subjectDTO)))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().response("Something went wrong").build());
        }
    }
}
