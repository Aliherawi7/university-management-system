package com.mycompany.portalapi.services;


import com.mycompany.portalapi.dtos.StudentPersonalInfo;
import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.dtos.StudentResponseDTO;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Relative;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final IdentificationService identificationService;
    private final LocationService locationService;
    private final RelativeService relativeService;
    private final FileStorageService fileStorageService;
    private final StudentResponseDTOMapper studentResponseDTOMapper;

    public void addStudent(StudentRegistrationDTO studentRegistrationDTO){
        /* save the student in db */
        StudentPersonalInfo studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        Student student = Student.builder()
                .name(studentPersonalInfo.name())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.now())
                .nationalId(studentRegistrationDTO.identification().nationalId())
                .motherTongue(studentPersonalInfo.motherTongue())
                .fieldOfStudy(studentPersonalInfo.fieldOfStudy())
                .department(studentPersonalInfo.department())
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .maritalStatus(studentPersonalInfo.maritalStatus())
                .build();
        student = studentRepository.save(student);
        Long studentId = student.getId();
        /* save the student current and previous locations in db */
        locationService.addLocation(Location
                .builder()
                .studentId(studentId)
                .city(studentRegistrationDTO.locations().current().city())
                .district(studentRegistrationDTO.locations().current().district())
                .villageOrQuarter(studentRegistrationDTO.locations().current().villageOrQuarter())
                .current(studentRegistrationDTO.locations().current().current())
                .build());
        locationService.addLocation(Location
                .builder()
                .studentId(studentId)
                .city(studentRegistrationDTO.locations().previous().city())
                .district(studentRegistrationDTO.locations().previous().district())
                .villageOrQuarter(studentRegistrationDTO.locations().previous().villageOrQuarter())
                .current(studentRegistrationDTO.locations().previous().current())
                .build());

        /* save the student relatives in db */

        studentRegistrationDTO.relatives().forEach(item -> {
            System.out.println(item.name());
            Relative relative = Relative
                    .builder()
                    .job(item.job())
                    .studentId(studentId)
                    .jobLocation(item.jobLocation())
                    .phoneNumber(item.phoneNumber())
                    .relation(item.relation())
                    .name(item.name())
                    .build();
            relativeService.addRelative(relative);
        });

        /* save the identification of the student */;
        Identification identification = Identification
                .builder()
                .studentId(studentId)
                .pageNumber(studentRegistrationDTO.identification().pageNumber())
                .caseNumber(studentRegistrationDTO.identification().caseNumber())
                .nationalId(studentRegistrationDTO.identification().nationalId())
                .registrationNumber(studentRegistrationDTO.identification().registrationNumber())
                .build();
        identificationService.addIdentification(identification);

        /* save the student image in student-profile directory */
        if(studentRegistrationDTO.studentImage() == null || studentRegistrationDTO.studentImage().isEmpty()){
            return;
        }
        fileStorageService.storeStudentProfileImage(studentRegistrationDTO.studentImage(), student.getId());

    }

    public StudentResponseDTO getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isEmpty()){
            throw new ResourceNotFoundException("student not found with provided id:"+studentId);
        }
        return studentResponseDTOMapper.apply(student.get());
    }
}
