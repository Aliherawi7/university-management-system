package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.MaritalStatusName;
import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Department;
import com.mycompany.portalapi.models.MaritalStatus;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.repositories.*;
import com.mycompany.portalapi.services.mappers.StudentResponseDTOMapper;
import com.mycompany.portalapi.services.mappers.StudentShortInfoMapper;
import com.mycompany.portalapi.utils.StudentUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
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
    private StudentService underTest;
    StudentRegistrationDTO studentRegistrationDTO;
    Student student;

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
                studentShortInfoMapper
        );
        studentRegistrationDTO = StudentRegistrationDTO
                .builder().build();
        student = Student.builder()
                .name("testName")
                .email("test@gmail.com")
                .lastName("lastname")
                .fatherName("")
                .grandFatherName("")
                .maritalStatus(MaritalStatus.builder().id(1).name(MaritalStatusName.MARRIED.name()).build())
                .department("dep")
                .dob(LocalDate.of(1990, 2, 1))
                .fieldOfStudy("field")
                .joinedDate(LocalDate.now())
                .motherTongue("Dari")
                .semester(2)
                .phoneNumber("4058234095")
                .highSchool("schoolName")
                .schoolGraduationDate(LocalDate.of(2017,2,1))
                .build();
    }

    @Test
    void addStudentForController() {

    }

    @Test
    void addStudent() {
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
        String email = "test@gmail.com";
        //when
        Optional<Student> studentOp = Optional.of(student);
        when(studentRepository.findByEmail(email)).thenReturn(studentOp);
        underTest.getStudentByEmail(email);
        //then
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