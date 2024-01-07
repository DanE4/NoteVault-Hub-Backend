package com.dane.notevault.controller;


import com.dane.notevault.entity.enums.FileExtension;
import com.dane.notevault.exception.InvalidFileException;
import com.dane.notevault.exception.StorageException;
import com.dane.notevault.exception.StorageFileNotFoundException;
import com.dane.notevault.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.EnumSet;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Validated
@Tag(name = "File")
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private long fileMaxSize = 1024 * 1024 * 10; // 10MB
    private final StorageService storageService;


    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    //TODO: fix swagger file upload
    @Operation(summary = "Upload a file",
            description = "This operation allows you to upload a file. The file is then stored and a success message is returned.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary", description = "The file to be uploaded"))
            ))
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") @Valid MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            log.info("File name: {}", file.getOriginalFilename());
            long size = file.getSize();
            if (size > fileMaxSize) {
                throw new InvalidFileException("File too large");
            }

            // Additional validation for file extension
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            if (!isValidFileExtension(originalFilename)) {
                throw new InvalidFileException("Invalid file extension");
            }

            storageService.store(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + originalFilename + "!");

            return "uploadForm";
        } catch (InvalidFileException e) {
            log.error("Invalid file: ", e);
            redirectAttributes.addFlashAttribute("message",
                    "Invalid file. Error: " + e.getMessage());
            return "redirect:/";
        } catch (StorageException e) {
            log.error("Could not store file.", e);
            redirectAttributes.addFlashAttribute("message",
                    "Could not store file. Error: " + e.getMessage());
            return "redirect:/";
        }

    }

    private boolean isValidFileExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        FileExtension fileExtension = FileExtension.valueOf(extension.toUpperCase());
        //make an array of valid file extensions
        return EnumSet.of(FileExtension.JPG, FileExtension.JPEG, FileExtension.PNG,
                        FileExtension.GIF, FileExtension.WEBP, FileExtension.TIFF,
                        FileExtension.RAW, FileExtension.HEIF, FileExtension.SVG,
                        FileExtension.PDF)
                .contains(fileExtension);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
   /* public ResponseEntity<Response> uploadFile(@PathVariable(value = "user_id") UUID userId) {
        return null;
    }
*/
