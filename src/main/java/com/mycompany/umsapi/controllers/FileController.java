package com.mycompany.umsapi.controllers;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.studentDto.StudentSuccessfulRegistrationResponse;
import com.mycompany.umsapi.exceptions.ErrorResponse;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.services.FileStorageService;
import com.mycompany.umsapi.services.GeneralPostService;
import com.mycompany.umsapi.services.StudentService;
import com.mycompany.umsapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;


@RestController
@RequestMapping("api/v1/files")
@AllArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;
    private final StudentService studentService;
    private final HttpServletRequest httpServletRequest;
    private final GeneralPostService generalPostService;

    @GetMapping("user-profiles/{studentId}")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable String studentId) {
        byte[] image = fileStorageService.getStudentImage(studentId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping("student-profiles/{studentId}")
    public ResponseEntity<?> addStudentImage(@PathVariable Long studentId, @RequestParam MultipartFile file) {
        if (!studentService.isExistById(studentId)) {
            throw new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!");
        }
        fileStorageService.storeStudentProfileImage(file, studentId);
        return new ResponseEntity<>(StudentSuccessfulRegistrationResponse
                .builder()
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + studentId)
                .statusCode(HttpStatus.CREATED.value())
                .message("محصل با موفقیت ذخیره شد.")
                .studentId(studentId)
                .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("student-profiles/{studentId}")
    public ResponseEntity<byte[]> getStudentProfilePicture(@PathVariable String studentId) {
        byte[] image = fileStorageService.getStudentImage(studentId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping("post-files/{postId}")
    public ResponseEntity<?> addPostFiles(@PathVariable Long postId, @RequestParam List<MultipartFile> files) {
        generalPostService.checkIfNotExist(postId);
        fileStorageService.storePostFiles(files, postId);
        return ResponseEntity.ok("پوسست همراه با فایل ها  موفقانه ذخیره شد");
    }

    @GetMapping("post-files/{postId}/{fileName}")
    public ResponseEntity<?> getPostFile(@PathVariable Long postId, @PathVariable String fileName) {
        System.err.println("post id: " + postId + " filename: " + fileName);
        if (fileName.endsWith("pdf")) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(fileStorageService.getPostFile(fileName, postId));
        } else if (fileName.endsWith("png")) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(fileStorageService.getPostFile(fileName, postId));
        } else if (fileName.endsWith("jpg") || fileName.endsWith("jpeg"))
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileStorageService.getPostFile(fileName, postId));
        return new ResponseEntity<>("فایل نامعتبر: این نوع فایل پشتیبانی نمیشود!", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("post-files/{postId}/{fileName}")
    public ResponseEntity<?> deletePostFile(@PathVariable Long postId, @PathVariable String fileName) {
        fileStorageService.deletePostFile(fileName, postId);
        return ResponseEntity.ok(ErrorResponse.builder()
                .message("فایل با موفقیت حذف شد!")
                .httpStatus(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .zonedDateTime(ZonedDateTime.now())
                .build());
    }


}
