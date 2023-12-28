package com.mycompany.umsapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.umsapi.constants.MaritalStatusName;
import com.mycompany.umsapi.dtos.IdentificationDTO;
import com.mycompany.umsapi.dtos.studentDto.StudentPersonalInfo;
import com.mycompany.umsapi.dtos.studentDto.StudentRegistrationDTO;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Faculty;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.models.hrms.Gender;
import com.mycompany.umsapi.models.hrms.MaritalStatus;
import com.mycompany.umsapi.models.hrms.Relationship;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.*;
import com.mycompany.umsapi.services.mappers.StudentResponseDTOMapper;
import com.mycompany.umsapi.services.mappers.StudentShortInfoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock private StudentRepository studentRepository;
    @Mock private IdentificationService identificationService;
    @Mock private LocationService locationService;
    @Mock private RelativeService relativeService;
    @Mock private FileStorageService fileStorageService;
    @Mock private StudentResponseDTOMapper studentResponseDTOMapper;
    @Mock private HttpServletRequest httpServletRequest;
    @Mock private AuthenticationService authenticationService;
    @Mock private RoleRepository roleRepository;
    @Mock private GenderRepository genderRepository;
    @Mock private MaritalStatusRepository maritalStatusRepository;
    @Mock private RelationshipRepository relationshipRepository;
    @Mock private RequestObjectValidatorService<StudentRegistrationDTO> studentRegistrationDTORequestValidatorService;
    @Mock private StudentShortInfoMapper studentShortInfoMapper;
    @Mock private DepartmentService departmentService;
    private StudentService underTest;
    StudentRegistrationDTO studentRegistrationDTO;
    Student student;
    IdentificationDTO identification;
    Department department;
    Semester semester;
    MaritalStatus maritalStatus;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(
                studentRepository,
                identificationService,
                locationService,
                relativeService,
                fileStorageService,
                studentResponseDTOMapper,
                httpServletRequest,
                authenticationService,
                roleRepository,
                genderRepository,
                maritalStatusRepository,
                relationshipRepository,
                studentRegistrationDTORequestValidatorService,
                studentShortInfoMapper,
                departmentService
        );

        ObjectMapper objectMapper = new ObjectMapper();
        URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/student.json");
        StudentRegistrationDTO[] students = null;
        try {
            students = objectMapper.readValue(jsonUrl, StudentRegistrationDTO[].class);
            studentRegistrationDTO = students[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Faculty faculty = Faculty.builder()
                .id(1L)
                .build();
        department = Department.builder()
                .departmentName("cs")
                .id(1L)
                .faculty(faculty)
                .build();
        semester = Semester.builder()
                .department(department)
                .semester(1).id(1L).build();
        maritalStatus = MaritalStatus.builder()
                .id(1)
                .name(MaritalStatusName.MARRIED.name())
                .build();

        studentRegistrationDTO = StudentRegistrationDTO
                .builder()
                .studentPersonalInfo(studentRegistrationDTO.studentPersonalInfo())
                .identification(studentRegistrationDTO.identification())
                .locations(studentRegistrationDTO.locations())
                .relatives(studentRegistrationDTO.relatives())
                .build();
        StudentPersonalInfo studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        student = Student.builder()
                .name(studentPersonalInfo.name())
                .email(studentPersonalInfo.email())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .maritalStatus(maritalStatus)
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.parse(studentPersonalInfo.joinedDate()))
                .motherTongue(studentPersonalInfo.motherTongue())
                .phoneNumber(studentPersonalInfo.phoneNumber())
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .build();
    }

    @Test
    void addStudentForController() {

    }

    @Test
    void addStudent() {
        student.setDepartment(department);
        student.setSemester(semester);

        for(int i = 0; i< studentRegistrationDTO.relatives().size(); i++){
            when(relationshipRepository.findById(studentRegistrationDTO.relatives().get(i).relationshipId()))
                    .thenReturn(Optional.ofNullable(Relationship.builder().build()));
        }
        when(maritalStatusRepository.findById(studentRegistrationDTO.studentPersonalInfo().maritalStatusId()))
                .thenReturn(Optional.ofNullable(maritalStatus));
        when(genderRepository.findById(studentRegistrationDTO.studentPersonalInfo().genderId()))
                .thenReturn(Optional.ofNullable(Gender.builder().build()));

        when(studentRepository.save(student)).thenReturn(student);

        Long id = underTest.addStudent(studentRegistrationDTO);
        verify(studentRegistrationDTORequestValidatorService).validate(studentRegistrationDTO);
        verify(underTest).validateTheRegistrationRequest(studentRegistrationDTO);
        verify(departmentService).getDepartmentById(studentRegistrationDTO.studentPersonalInfo().department());
        verify(departmentService)
                .getSemesterByIdAndDepartment(studentRegistrationDTO.studentPersonalInfo().semester(), department);
        verify(maritalStatusRepository).findById(studentRegistrationDTO.studentPersonalInfo().maritalStatusId());
        verify(studentRepository).save(student);
        verify(locationService).addStudentLocation(student, studentRegistrationDTO.locations().current());
        verify(locationService).addStudentLocation(student, studentRegistrationDTO.locations().previous());
        verify(relativeService).addStudentRelatives(student, studentRegistrationDTO.relatives());

        assertTrue(id > 0);
    }

    @Test
    void updateStudent() {
    }

    @Test
    void getStudentByIdIfStudentIsExist() {
        //given
        long studentId = 2L;
        //when
        Student student = Student.builder()
                .id(studentId)
                .build();
        Optional<Student> studentOp = Optional.of(student);
        when(studentRepository.findById(studentId)).thenReturn(studentOp);
        underTest.getStudentById(studentId);

        //then
        verify(studentRepository).findById(studentId);

    }

    @Test
    void getStudentByIdIfStudentIsExistIsNotExist() {
        //given
        long studentId = 2L;
        //when
        Optional<Student> studentOp = Optional.empty();
        when(studentRepository.findById(studentId)).thenReturn(studentOp);
        assertThrows(ResourceNotFoundException.class,() -> underTest.getStudentById(studentId));
        //then
        verify(studentRepository).findById(studentId);
    }

    @Test
    void getStudentByEmailIfStudentIsExist() {
        //given
        String email = student.getEmail();
        //when
        Optional<Student> studentOp = Optional.of(student);
        when(studentRepository.findByEmail(email)).thenReturn(studentOp);
        var std = underTest.getStudentByEmail(email);
        //then
        assertEquals(email, std.getEmail());
        verify(studentRepository).findByEmail(email);

    }

    @Test
    void getStudentByEmailIfStudentIsNotExist() {
        //given
        String email = "test@gmail.com";
        //when
        Optional<Student> studentOp = Optional.empty();
        when(studentRepository.findByEmail(email)).thenReturn(studentOp);
        assertThrows(ResourceNotFoundException.class, () -> underTest.getStudentByEmail(email));
        verify(studentRepository).findByEmail(email);

    }

    @Test
    void getStudentProfileIfStudentIsExist() {
        Long studentId = 2L;
        String email = "test@gmail.com";
        Optional<Student> studentOptional = Optional.of(student);
        when(studentRepository.findById(studentId)).thenReturn(studentOptional);
        String expected = underTest.getStudentProfile(studentId).email();
        assertEquals(email, expected);
    }
    @Test
    void getStudentProfileIfStudentIsNotExist() {
        Long studentId = 2L;
        String email = "test@gmail.com";
        Optional<Student> studentOptional = Optional.empty();
        when(studentRepository.findById(studentId)).thenReturn(studentOptional);
        assertThrows(ResourceNotFoundException.class, () -> underTest.getStudentProfile(studentId));
    }

    @Test
    void getAllStudents() {
    }

    @Test
    void mapStudentToStudentShortInfo() {
    }

    @Test
    void isExistById() {
    }

    @Test
    void isExistByEmail() {
    }

    @Test
    void validateTheRegistrationRequest() {
    }

    @Test
    void getAllStudentsByRequestParams() {
    }

    @Test
    void deleteStudentById() {
    }
}