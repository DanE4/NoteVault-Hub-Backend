package com.dane.homework_help.service.impl;

import com.dane.homework_help.exception.StorageException;
import com.dane.homework_help.exception.StorageFileNotFoundException;
import com.dane.homework_help.service.StorageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private String location = "src/main/resources/uploads";

    private final Path rootLocation;

    public StorageServiceImpl() {
        if (this.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }
        this.rootLocation = Paths.get(getLocation());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("prefix", "suffix");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String getFormatName(File imageFile) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

        if (!imageReaders.hasNext()) {
            throw new IllegalArgumentException("Cannot determine format of image");
        }

        ImageReader reader = imageReaders.next();
        iis.close();
        log.info("Format name: " + reader.getFormatName());
        return reader.getFormatName();
    }

    public File compressImage(File inputImage, float quality, Path destinationPath) throws IOException {
        InputStream inputStream = new FileInputStream(inputImage);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(getFormatName(inputImage));

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = imageWriters.next();

        // Create an in-memory ByteArrayOutputStream to hold the compressed image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        // Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(quality);

        // Created image
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        // close all streams
        inputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();

        // Convert compressed image in ByteArrayOutputStream to File
        File compressedImageFile = new File(destinationPath.toString());
        try (FileOutputStream fos = new FileOutputStream(compressedImageFile)) {
            fos.write(byteArrayOutputStream.toByteArray());
        }

        return compressedImageFile;
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destination = this.rootLocation.resolve(
                            Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();

            if (!destination.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                log.info("Compressing image...");
                File fileToCompress = convertMultiPartToFile(file);
                File compressedFile = compressImage(fileToCompress, 0.5f, destination);
                log.info("Compressed image stored at: " + destination.toString());
            } else {
                log.info("Storing non-image type file...");
                // Handle storing non-image file if needed
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new StorageFileNotFoundException("Could not read or find file: " + filename);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }


    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
