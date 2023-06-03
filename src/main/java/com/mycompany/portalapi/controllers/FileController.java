package com.mycompany.portalapi.controllers;
import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.StudentSuccessfulRegistrationResponse;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.services.FileStorageService;
import com.mycompany.portalapi.services.StudentService;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("api/v1/files")
@AllArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    private final  StudentService studentService;
    private final HttpServletRequest httpServletRequest;


    @GetMapping("user-profiles/{studentId}")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable String studentId) {
        byte[] image = fileStorageService.getStudentImage(studentId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping("student-profiles/{studentId}")
    public ResponseEntity<?> addStudentImage(@PathVariable Long studentId, @ModelAttribute MultipartFile file){
        if(!studentService.isExistById(studentId)){
            throw new ResourceNotFoundException("student not found with provided id: "+studentId);
        }
        fileStorageService.storeStudentProfileImage(file, studentId);
        return new ResponseEntity<>(StudentSuccessfulRegistrationResponse
                .builder()
                .imageUrl(BaseURI.getBaseURI(httpServletRequest)+ APIEndpoints.STUDENT_PROFILE_IMAGE.getValue()+studentId)
                .statusCode(HttpStatus.CREATED.value())
                .message("image successfully saved")
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






}
