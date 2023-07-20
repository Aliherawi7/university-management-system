package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.*;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.*;
import com.mycompany.portalapi.repositories.*;
import com.mycompany.portalapi.services.mappers.StudentResponseDTOMapper;
import com.mycompany.portalapi.services.mappers.StudentShortInfoMapper;
import com.mycompany.portalapi.utils.BaseURI;
import com.mycompany.portalapi.utils.StudentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
    private final GenderRepository genderRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final RelationshipRepository relationshipRepository;
    private final RequestObjectValidatorService<StudentRegistrationDTO> studentRegistrationDTORequestValidatorService;
    private final StudentShortInfoMapper studentShortInfoMapper;

    public StudentSuccessfulRegistrationResponse addStudentForController(StudentRegistrationDTO studentRegistrationDTO) {
        Long studentId = addStudent(studentRegistrationDTO);
        return StudentSuccessfulRegistrationResponse.builder().message("student successfully saved in db").statusCode(HttpStatus.CREATED.value()).studentId(studentId).imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + studentId).build();
    }

    public Long addStudent(StudentRegistrationDTO studentRegistrationDTO) {
        studentRegistrationDTORequestValidatorService.validate(studentRegistrationDTO);
        validateTheRegistrationRequest(studentRegistrationDTO);
        /* save the student in db */
        StudentPersonalInfo studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        MaritalStatus maritalStatus = maritalStatusRepository.findByName(studentPersonalInfo.maritalStatus()).orElseThrow(() -> new IllegalArgumentException("نوع حالت مدنی نامعتبر است!"));
        Student student = Student.builder()
                .name(studentPersonalInfo.name())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.now())
                .motherTongue(studentPersonalInfo.motherTongue())
                .fieldOfStudy(studentPersonalInfo.fieldOfStudy())
                .department(studentPersonalInfo.department())
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .maritalStatus(maritalStatus)
                .email(studentPersonalInfo.email().toLowerCase())
                .password(studentPersonalInfo.password())
                .semester(studentPersonalInfo.semester())
                .phoneNumber(studentPersonalInfo.phoneNumber())
                .build();
        student = studentRepository.save(student);
        Long studentId = student.getId();
        /* save the student current and previous locations in db */
        locationService.addLocation(Location.builder().student(student).city(studentRegistrationDTO.locations().current().city()).district(studentRegistrationDTO.locations().current().district()).villageOrQuarter(studentRegistrationDTO.locations().current().villageOrQuarter()).current(studentRegistrationDTO.locations().current().current()).build());
        locationService.addLocation(Location.builder().student(student).city(studentRegistrationDTO.locations().previous().city()).district(studentRegistrationDTO.locations().previous().district()).villageOrQuarter(studentRegistrationDTO.locations().previous().villageOrQuarter()).current(studentRegistrationDTO.locations().previous().current()).build());

        /* save the student relatives in db */

        Student finalStudent = student;
        studentRegistrationDTO.relatives().forEach(item -> {
            Relationship relationship = relationshipRepository.findByName(item.relationship()).orElseThrow(() -> new IllegalArgumentException("نوعیت اقارب نامعتبر"));
            Relative relative = Relative.builder().job(item.job()).student(finalStudent).jobLocation(item.jobLocation()).phoneNumber(item.phoneNumber())
                    .relationship(relationship).name(item.name()).build();
            relativeService.addRelative(relative);
        });

        /* save the identification of the student */
        Identification identification = Identification.builder().student(student).pageNumber(studentRegistrationDTO.identification().pageNumber()).caseNumber(studentRegistrationDTO.identification().caseNumber()).nationalId(studentRegistrationDTO.identification().nationalId()).registrationNumber(studentRegistrationDTO.identification().registrationNumber()).build();
        identificationService.addIdentification(identification);
        student.setIdentification(identification);
        /* prepare the student gender to save */
        Optional<Gender> gender = genderRepository.findByName(studentRegistrationDTO.studentPersonalInfo().gender());

        student.setGender(gender.orElseThrow(() -> new IllegalArgumentException("نوع جنسیت نامعتبر")));
        studentRepository.save(student);
        // RoleName roleName = RoleName.valueOf(studentRegistrationDTO.role());
        Optional<Role> role = roleRepository.findByRoleName(RoleName.STUDENT);

        authenticationService.registerUser(UserApp
                .builder()
                .id(studentId)
                .email(student.getEmail().toLowerCase())
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
            throw new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!");
        }
        return studentResponseDTOMapper.apply(student.get());
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.
                findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("محصل با ایمیل مورد نظر یافت نشد!"));
    }

    public StudentResponsePersonalInfo getStudentProfile(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!"));
        // نام، تخلص، نام پدر، شماره تماس، ایمیل، سال شمولیت به موسسه، کدام پوهنحی، کدام سمستر، سال چند
        return StudentResponsePersonalInfo
                .builder()
                .name(student.getName())
                .lastName(student.getLastName())
                .fatherName(student.getFatherName())
                .grandFatherName(student.getGrandFatherName())
                .maritalStatus(student.getMaritalStatus().getName())
                .department(student.getDepartment())
                .fieldOfStudy(student.getFieldOfStudy())
                .email(student.getEmail())
                .joinedDate(student.getJoinedDate())
                .motherTongue(student.getMotherTongue())
                .semester(student.getSemester())
                .year(StudentUtils.getYear(student.getSemester()))
                .phoneNumber(student.getPhoneNumber())
                .build();
    }

    public Page<StudentShortInfo> getAllStudents(int offset, int pageSize) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(offset, pageSize));
        return studentPage.map(studentShortInfoMapper);
    }

    public List<StudentShortInfo> mapStudentToStudentShortInfo(List<Student> students) {
        return students.stream().map(student -> {
            return StudentShortInfo.builder()
                    .name(student.getName())
                    .lastname(student.getLastName())
                    .fieldStudy(student.getFieldOfStudy())
                    .department(student.getDepartment())
                    .id(student.getId())
                    .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId()+".png").build();
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
            throw new IllegalArgumentException("نمبر عمومی تذکره از قبل موجود است!");
        }
        /* check if email is already taken */
        if (studentRepository.existsByEmail(studentRegistrationDTO.studentPersonalInfo().email())) {
            throw new IllegalArgumentException("the email: { " + studentRegistrationDTO.studentPersonalInfo().email() + " } is already exist");
        }
        studentRegistrationDTO.relatives().forEach(item -> {
            Relationship relationship = relationshipRepository
                    .findByName(item.relationship()).orElseThrow(() -> new IllegalArgumentException("نوعیت اقارب نامعتبر"));
        });
    }

    /* search methods */

    public Page<StudentShortInfo> getAllPostsByRequestParams(
            String keyword,
            String fieldOfStudy,
            String department,
            Integer semester,
            int offset,
            int pageSize
    ) {
        Page<Student> students = null;
        PageRequest pageRequest = PageRequest.of(offset, pageSize);
        if (semester != null) {
            students = studentRepository.fetchAllStudentByKeywordAndFieldOfStudyAndDepartmentAndSemester(
                    keyword, fieldOfStudy, department, semester, pageRequest
            );
        } else {
            students = studentRepository.fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(
                    keyword, fieldOfStudy, department, pageRequest
            );
        }
        return students.map(studentShortInfoMapper);
    }
}
