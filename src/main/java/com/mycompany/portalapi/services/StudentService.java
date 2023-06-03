package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.*;
import com.mycompany.portalapi.repositories.RoleRepository;
import com.mycompany.portalapi.repositories.StudentRepository;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private final HttpServletRequest httpServletRequest;
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;

    public StudentSuccessfulRegistrationResponse addStudentForController(StudentRegistrationDTO studentRegistrationDTO) {
        Long studentId = addStudent(studentRegistrationDTO);
        return StudentSuccessfulRegistrationResponse.builder().message("student successfully saved in db").statusCode(HttpStatus.CREATED.value()).studentId(studentId).imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + studentId).build();
    }

    public Long addStudent(StudentRegistrationDTO studentRegistrationDTO) {
        validateTheRegistrationRequest(studentRegistrationDTO);
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
                .email(studentPersonalInfo.email())
                .password(studentPersonalInfo.password()).build();
        student = studentRepository.save(student);
        Long studentId = student.getId();
        /* save the student current and previous locations in db */
        locationService.addLocation(Location.builder().studentId(studentId).city(studentRegistrationDTO.locations().current().city()).district(studentRegistrationDTO.locations().current().district()).villageOrQuarter(studentRegistrationDTO.locations().current().villageOrQuarter()).current(studentRegistrationDTO.locations().current().current()).build());
        locationService.addLocation(Location.builder().studentId(studentId).city(studentRegistrationDTO.locations().previous().city()).district(studentRegistrationDTO.locations().previous().district()).villageOrQuarter(studentRegistrationDTO.locations().previous().villageOrQuarter()).current(studentRegistrationDTO.locations().previous().current()).build());

        /* save the student relatives in db */

        studentRegistrationDTO.relatives().forEach(item -> {
            System.out.println(item.name());
            Relative relative = Relative.builder().job(item.job()).studentId(studentId).jobLocation(item.jobLocation()).phoneNumber(item.phoneNumber()).relation(item.relation()).name(item.name()).build();
            relativeService.addRelative(relative);
        });

        /* save the identification of the student */
        ;
        Identification identification = Identification.builder().studentId(studentId).pageNumber(studentRegistrationDTO.identification().pageNumber()).caseNumber(studentRegistrationDTO.identification().caseNumber()).nationalId(studentRegistrationDTO.identification().nationalId()).registrationNumber(studentRegistrationDTO.identification().registrationNumber()).build();
        identificationService.addIdentification(identification);
        System.err.println(student);
        RoleName roleName = RoleName.valueOf(studentRegistrationDTO.role().toUpperCase());
        System.err.println(roleName);
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        authenticationService.registerUser(User
                .builder()
                .id(studentId)
                .email(student.getEmail())
                .roles(List.of(role.orElse(roleRepository.findByRoleName(RoleName.STUDENT).get())))
                .password(student.getPassword())
                .isEnabled(true)
                .lastname(student.getLastName())
                .name(student.getName())
                .build());
        return studentId;
    }

    public StudentResponseDTO getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("student not found with provided id:" + studentId);
        }
        return studentResponseDTOMapper.apply(student.get());
    }

    public PageContainerDTO<StudentShortInfo> getAllStudents(int offset, int pageSize) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(offset, pageSize));
        if (studentPage.getSize() == 0) {
            return new PageContainerDTO<StudentShortInfo>(0, new ArrayList<>());
        }
        return new PageContainerDTO<>(studentRepository.count(), mapStudentToStudentShortInfo(studentPage.toList()));
    }

    public List<StudentShortInfo> mapStudentToStudentShortInfo(List<Student> students) {
        return students.stream().map(student -> {
            return StudentShortInfo.builder().name(student.getName()).lastname(student.getLastName()).fieldStudy(student.getFieldOfStudy()).department(student.getDepartment()).imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId()).build();
        }).toList();
    }


    public boolean isExistById(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    public boolean isExistByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }


    public void validateTheRegistrationRequest(StudentRegistrationDTO studentRegistrationDTO) {
        /* check if national id is already taken */
        if (identificationService.isNationalIdAlreadyExist(studentRegistrationDTO.identification().nationalId())) {
            throw new IllegalArgumentException("the national id is already exist");
        }
        /* check if email is already taken */
    }


}
