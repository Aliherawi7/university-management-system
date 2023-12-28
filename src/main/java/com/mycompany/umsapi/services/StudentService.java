package com.mycompany.umsapi.services;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.studentDto.*;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.hrms.Gender;
import com.mycompany.umsapi.models.hrms.Identification;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.*;
import com.mycompany.umsapi.services.mappers.StudentResponseDTOMapper;
import com.mycompany.umsapi.services.mappers.StudentShortInfoMapper;
import com.mycompany.umsapi.utils.BaseURI;
import com.mycompany.umsapi.utils.StudentUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final GenderRepository genderRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final RelationshipRepository relationshipRepository;
    private final RequestObjectValidatorService<StudentRegistrationDTO> studentRegistrationDTORequestValidatorService;
    private final StudentShortInfoMapper studentShortInfoMapper;
    private final DepartmentService departmentService;

    public StudentSuccessfulRegistrationResponse addStudentForController(StudentRegistrationDTO studentRegistrationDTO) {
        Long studentId = addStudent(studentRegistrationDTO);
        return StudentSuccessfulRegistrationResponse.builder()
                .message("student successfully saved!")
                .statusCode(HttpStatus.CREATED.value())
                .studentId(studentId)
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + studentId)
                .build();
    }

    public Long addStudent(StudentRegistrationDTO studentRegistrationDTO) {
        studentRegistrationDTORequestValidatorService.validate(studentRegistrationDTO);
        validateTheRegistrationRequest(studentRegistrationDTO);

        /* check if department, semester and marital-status are exist in db */
        var studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        var department = departmentService.getDepartmentById(studentPersonalInfo.department());
        var semester = departmentService.getSemesterByIdAndDepartment(studentPersonalInfo.semester(), department);
        var maritalStatus = maritalStatusRepository.findById(studentPersonalInfo.maritalStatusId()).orElseThrow(() -> new IllegalArgumentException("نوع حالت مدنی نامعتبر است!"));

        var student = Student.builder()
                .name(studentPersonalInfo.name())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.parse(studentPersonalInfo.joinedDate()))
                .motherTongue(studentPersonalInfo.motherTongue())
                .department(department)
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .maritalStatus(maritalStatus)
                .email(studentPersonalInfo.email().toLowerCase())
                .password(studentPersonalInfo.password())
                .semester(semester)
                .phoneNumber(studentPersonalInfo.phoneNumber())
                .build();

        /* save the identification of the student */
        Identification identification = identificationService
                .addIdentificationDTO(studentRegistrationDTO.identification());
        student.setIdentification(identification);
        /* prepare the student gender to save */
        Gender gender = genderRepository.findById(studentRegistrationDTO.studentPersonalInfo().genderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid gender type"));
        student.setGender(gender);

        /* save the student in db */
        student = studentRepository.save(student);

        /* save the student current and previous locations in db */
        locationService.addStudentLocation(student, studentRegistrationDTO.locations().current());
        locationService.addStudentLocation(student, studentRegistrationDTO.locations().previous());

        /* save the student relatives in db */
        relativeService.addStudentRelatives(student, studentRegistrationDTO.relatives());

        /* add student as a user in database for authentication purpose */
        userService.registerStudentAsUser(student);
        return student.getId();
    }

    public StudentSuccessfulRegistrationResponse updateStudent(Long id, StudentRegistrationDTO studentRegistrationDTO) {
        /* we should consider to check the email and nationalId duplication in here */
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("student not found with provided id!"));
        var studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        var department = departmentService.getDepartmentById(studentPersonalInfo.department());
        var semester = departmentService.getSemesterByIdAndDepartment(studentPersonalInfo.semester(), department);
        var maritalStatus = maritalStatusRepository
                .findById(studentPersonalInfo.maritalStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid marital status id!"));
        student.setName(studentPersonalInfo.name());
        student.setLastName(studentPersonalInfo.lastName());
        student.setFatherName(studentPersonalInfo.fatherName());
        student.setGrandFatherName(studentPersonalInfo.grandFatherName());
        student.setDob(LocalDate.parse(studentPersonalInfo.dob()));
        student.setJoinedDate(LocalDate.now());
        student.setMotherTongue(studentPersonalInfo.motherTongue());
        student.setDepartment(department);
        student.setHighSchool(studentPersonalInfo.highSchool());
        student.setSchoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()));
        student.setMaritalStatus(maritalStatus);
        student.setEmail(studentPersonalInfo.email().toLowerCase());
        student.setPassword(studentPersonalInfo.password());
        student.setSemester(semester);
        student.setPhoneNumber(studentPersonalInfo.phoneNumber());

        /* update the identification of the student */
        identificationService.updateStudentIdentification(student, studentRegistrationDTO.identification());

        /* prepare the student gender to save */
        Gender gender = genderRepository.findById(studentRegistrationDTO.studentPersonalInfo().genderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid gender type"));
        student.setGender(gender);

        /* update the student relatives in db */
        relativeService.updateStudentRelatives(student, studentRegistrationDTO.relatives());

        /* save the student current and previous locations in db */
        locationService.updateStudentLocation(student, studentRegistrationDTO.locations());

        return StudentSuccessfulRegistrationResponse.builder()
                .message("student updated successfully")
                .statusCode(HttpStatus.CREATED.value())
                .studentId(student.getId())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId())
                .build();
    }

    public Student getStudentById(Long studentId) {
        return studentRepository
                .findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("student not found with provided id!"));
    }

    public StudentResponseDTO getStudentResponseDTOById(Long studentId) {
        return studentResponseDTOMapper
                .apply(studentRepository
                        .findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("student not found with provided id!")));
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.
                findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("student not found with provided email!"));
    }

    public StudentResponsePersonalInfo getStudentProfile(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with provided id!"));
        // نام، تخلص، نام پدر، شماره تماس، ایمیل، سال شمولیت به موسسه، کدام پوهنحی، کدام سمستر، سال چند
        return StudentResponsePersonalInfo
                .builder()
                .name(student.getName())
                .lastName(student.getLastName())
                .fatherName(student.getFatherName())
                .grandFatherName(student.getGrandFatherName())
                .maritalStatus(student.getMaritalStatus().getName())
                .department(student.getDepartment().getDepartmentName())
                .faculty(student.getDepartment().getFaculty().getFacultyName())
                .email(student.getEmail())
                .joinedDate(student.getJoinedDate())
                .motherTongue(student.getMotherTongue())
                .semester(student.getSemester().getSemester())
                .year(StudentUtils.getYear(student.getSemester().getSemester()))
                .phoneNumber(student.getPhoneNumber())
                .dob(student.getDob().toString())
                .highSchool(student.getHighSchool())
                .schoolGraduationDate(student.getSchoolGraduationDate().toString())
                .build();
    }

    public Page<StudentShortInfo> getAllStudents(int offset, int pageSize) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(offset, pageSize));
        return studentPage.map(studentShortInfoMapper);
    }

    public List<StudentShortInfo> mapStudentToStudentShortInfo(List<Student> students) {
        return students.stream().map(student -> StudentShortInfo.builder()
                        .name(student.getName())
                        .lastname(student.getLastName())
                        .faculty(student.getDepartment().getFaculty().getFacultyName())
                        .department(student.getDepartment().getDepartmentName())
                        .id(student.getId())
                        .imageUrl(BaseURI
                                .getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId() + ".png")
                        .build())
                .toList();
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
            throw new IllegalArgumentException("Identification number is already exist!");
        }
        /* check if email is already taken */
        if (studentRepository.existsByEmail(studentRegistrationDTO.studentPersonalInfo().email())) {
            throw new IllegalArgumentException("Email is already exist!");
        }
        studentRegistrationDTO.relatives().forEach(item -> {
            relationshipRepository
                    .findById(item.relationshipId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid relationship id!"));
        });
    }

    /* search methods */

    public Page<StudentShortInfo> getAllStudentsByRequestParams(
            String keyword,
            Long faculty,
            Long department,
            Long semester,
            int offset,
            int pageSize
    ) {
        Page<Student> students = null;
        PageRequest pageRequest = PageRequest.of(offset, pageSize);
        if (semester != null) {
            students = studentRepository.fetchAllStudentByKeywordAndFieldOfStudyAndDepartmentAndSemester(
                    keyword, department, semester, pageRequest
            );
        } else {
            students = studentRepository.fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(
                    keyword, department, pageRequest
            );
        }
        return students.map(studentShortInfoMapper);
    }

    @Transactional
    public void deleteStudentById(Long studentId) {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with provided id!"));
        relativeService.deleteAllRelativesByStudent(studentId);
        userService.deleteUser(student.getEmail());
        locationService.deleteLocationsByStudentId(studentId);
        identificationService.deleteIdentificationById(student.getIdentification().getId());
        studentRepository.deleteById(studentId);

        //TODO: must remove the student image

    }

    public Student mapStudentInfoDTOToStudent(StudentPersonalInfo studentPersonalInfo) {
        return Student.builder()
                .name(studentPersonalInfo.name())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.parse(studentPersonalInfo.joinedDate()))
                .motherTongue(studentPersonalInfo.motherTongue())
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .email(studentPersonalInfo.email().toLowerCase())
                .password(studentPersonalInfo.password())
                .phoneNumber(studentPersonalInfo.phoneNumber())
                .build();
    }
}
