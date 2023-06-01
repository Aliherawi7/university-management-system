package com.mycompany.portalapi.controllers;
import com.mycompany.portalapi.services.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("user-profiles/{studentId}")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable String studentId) {
        byte[] image = fileStorageService.getStudentImage(studentId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
    @GetMapping("student-profiles/{studentId}")
    public ResponseEntity<byte[]> getStudentProfilePicture(@PathVariable String studentId) {
        byte[] image = fileStorageService.getStudentImage(studentId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }


}
