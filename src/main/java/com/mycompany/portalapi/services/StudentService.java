package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.constants.RelationName;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.studentDto.*;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.hrms.*;
import com.mycompany.portalapi.repositories.*;
import com.mycompany.portalapi.services.mappers.StudentResponseDTOMapper;
import com.mycompany.portalapi.services.mappers.StudentShortInfoMapper;
import com.mycompany.portalapi.utils.BaseURI;
import com.mycompany.portalapi.utils.StudentUtils;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private final FacultyService facultyService;

    public StudentSuccessfulRegistrationResponse addStudentForController(StudentRegistrationDTO studentRegistrationDTO) {
        Long studentId = addStudent(studentRegistrationDTO);
        return StudentSuccessfulRegistrationResponse.builder()
                .message("محصل موفقانه ثبت شد!")
                .statusCode(HttpStatus.CREATED.value())
                .studentId(studentId)
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + studentId)
                .build();
    }

    public Long addStudent(StudentRegistrationDTO studentRegistrationDTO) {
        studentRegistrationDTORequestValidatorService.validate(studentRegistrationDTO);
        validateTheRegistrationRequest(studentRegistrationDTO);

        /* save the student in db */
        StudentPersonalInfo studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        Faculty faculty = facultyService.getById(studentPersonalInfo.faculty());
        Department department = faculty.getDepartments()
                .stream()
                .filter(d -> Objects.equals(d.getId(), studentPersonalInfo.department()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("invalid department id"));
        Semester semester = department.getSemesters().stream()
                .filter(item -> item.getSemester() == studentPersonalInfo.semester())
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid semester number!"));
        MaritalStatus maritalStatus = maritalStatusRepository.findByName(studentPersonalInfo.maritalStatus()).orElseThrow(() -> new IllegalArgumentException("نوع حالت مدنی نامعتبر است!"));
        Student student = Student.builder()
                .name(studentPersonalInfo.name())
                .lastName(studentPersonalInfo.lastName())
                .fatherName(studentPersonalInfo.fatherName())
                .grandFatherName(studentPersonalInfo.grandFatherName())
                .dob(LocalDate.parse(studentPersonalInfo.dob()))
                .joinedDate(LocalDate.parse(studentPersonalInfo.joinedDate()))
                .motherTongue(studentPersonalInfo.motherTongue())
                .faculty(faculty)
                .department(department)
                .highSchool(studentPersonalInfo.highSchool())
                .schoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()))
                .maritalStatus(maritalStatus)
                .email(studentPersonalInfo.email().toLowerCase())
                .password(studentPersonalInfo.password())
                .semester(semester)
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
            Relationship relationship = relationshipRepository.findByRelationName(RelationName.valueOf(item.relationship())).orElseThrow(() -> new IllegalArgumentException("نوعیت اقارب نامعتبر"));
            Relative relative = Relative.builder().job(item.job()).student(finalStudent).jobLocation(item.jobLocation()).phoneNumber(item.phoneNumber())
                    .relationship(relationship).name(item.name()).build();
            relativeService.addRelative(relative);
        });

        /* save the identification of the student */
        Identification identification = Identification.builder()
                .pageNumber(studentRegistrationDTO.identification().pageNumber())
                .caseNumber(studentRegistrationDTO.identification().caseNumber())
                .nationalId(studentRegistrationDTO.identification().nationalId())
                .registrationNumber(studentRegistrationDTO.identification().registrationNumber())
                .build();
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
                .roles(Set.of(role.orElse(roleRepository.findByRoleName(RoleName.STUDENT).get())))
                .password(student.getPassword())
                .isEnabled(true)
                .lastname(student.getLastName())
                .name(student.getName())
                .build());
        return studentId;
    }

    public StudentSuccessfulRegistrationResponse updateStudent(Long id, StudentRegistrationDTO studentRegistrationDTO) {
        /* we should consider to check the email and nationalId duplication in here */
        var student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!"));
        var studentPersonalInfo = studentRegistrationDTO.studentPersonalInfo();
        var faculty = facultyService.getById(studentPersonalInfo.faculty());
        var department = faculty.getDepartments()
                .stream()
                .filter(d -> Objects.equals(d.getId(), studentPersonalInfo.department()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("invalid department id"));
        var semester = department.getSemesters().stream()
                .filter(item -> Objects.equals(item.getSemester(), studentPersonalInfo.semester()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid semester number!"));
        var maritalStatus = maritalStatusRepository.findByName(studentPersonalInfo.maritalStatus()).orElseThrow(() -> new IllegalArgumentException("نوع حالت مدنی نامعتبر است!"));
        student.setName(studentPersonalInfo.name());
        student.setLastName(studentPersonalInfo.lastName());
        student.setFatherName(studentPersonalInfo.fatherName());
        student.setGrandFatherName(studentPersonalInfo.grandFatherName());
        student.setDob(LocalDate.parse(studentPersonalInfo.dob()));
        student.setJoinedDate(LocalDate.now());
        student.setMotherTongue(studentPersonalInfo.motherTongue());
        student.setFaculty(faculty);
        student.setDepartment(department);
        student.setHighSchool(studentPersonalInfo.highSchool());
        student.setSchoolGraduationDate(LocalDate.parse(studentPersonalInfo.schoolGraduationDate()));
        student.setMaritalStatus(maritalStatus);
        student.setEmail(studentPersonalInfo.email().toLowerCase());
        student.setPassword(studentPersonalInfo.password());
        student.setSemester(semester);
        student.setPhoneNumber(studentPersonalInfo.phoneNumber());

        /* update the identification of the student */
        Identification identification = student.getIdentification();
        identification.setCaseNumber(studentRegistrationDTO.identification().caseNumber());
        identification.setNationalId(studentRegistrationDTO.identification().nationalId());
        identification.setPageNumber(studentRegistrationDTO.identification().pageNumber());
        identification.setRegistrationNumber(studentRegistrationDTO.identification().registrationNumber());
        identificationService.addIdentification(identification);

        /* prepare the student gender to save */
        Gender gender = genderRepository.findByName(studentRegistrationDTO.studentPersonalInfo().gender()).orElseThrow(() -> new ResourceNotFoundException("نوع جنسیت نامعتبر است"));
        student.setGender(gender);

        /* update the student relatives in db */
        List<Relative> relativeList = relativeService.getAllStudentRelativesById(student.getId());
        studentRegistrationDTO.relatives().forEach(item -> {
            Relationship relationship = relationshipRepository.findByRelationName(RelationName.valueOf(item.relationship())).orElseThrow(() -> new IllegalArgumentException("نوعیت اقارب نامعتبر"));
            Relative relative = relativeList.stream().filter(r -> r.getRelationship().equals(relationship)).findFirst().get();
            relative.setName(item.name());
            relative.setJob(item.job());
            relative.setPhoneNumber(item.phoneNumber());
            relative.setJobLocation(item.jobLocation());
            relativeService.addRelative(relative);
        });

        /* save the student current and previous locations in db */
        List<Location> locations = locationService.getAllLocationsByStudentId(student.getId());
        locations.forEach(item -> {
            if (item.isCurrent()) {
                item.setCity(studentRegistrationDTO.locations().current().city());
                item.setDistrict(studentRegistrationDTO.locations().current().city());
                item.setVillageOrQuarter(studentRegistrationDTO.locations().current().city());
            } else {
                item.setCity(studentRegistrationDTO.locations().previous().city());
                item.setDistrict(studentRegistrationDTO.locations().previous().city());
                item.setVillageOrQuarter(studentRegistrationDTO.locations().previous().city());
            }
            locationService.addLocation(item);
        });
        return StudentSuccessfulRegistrationResponse.builder()
                .message("محصل موفقانه بروزرسانی شد!")
                .statusCode(HttpStatus.CREATED.value())
                .studentId(student.getId())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId())
                .build();
    }

    public Student getStudentById(Long studentId) {
        return studentRepository
                .findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!"));
    }

    public StudentResponseDTO getStudentResponseDTOById(Long studentId) {
        return studentResponseDTOMapper
                .apply(studentRepository
                        .findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("محصل با آی دی مورد نظر یافت نشد!")));
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
                .department(student.getDepartment().getDepartmentName())
                .faculty(student.getFaculty().getFacultyName())
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
        return students.stream().map(student -> {
            return StudentShortInfo.builder()
                    .name(student.getName())
                    .lastname(student.getLastName())
                    .faculty(student.getFaculty().getFacultyName())
                    .department(student.getDepartment().getDepartmentName())
                    .id(student.getId())
                    .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId() + ".png").build();
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
            throw new IllegalArgumentException("ایمیل از قبل موجود است!");
        }
        studentRegistrationDTO.relatives().forEach(item -> {
            relationshipRepository
                    .findByRelationName(RelationName.valueOf(item.relationship()))
                    .orElseThrow(() -> new IllegalArgumentException("نوعیت اقارب نامعتبر"));
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
                    keyword, faculty, department, semester, pageRequest
            );
        } else {
            students = studentRepository.fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(
                    keyword, faculty, department, pageRequest
            );
        }
        return students.map(studentShortInfoMapper);
    }

    @Transactional
    public void deleteStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("محصل مورد نظر یافت نشد!"));
        relativeService.deleteAllRelativesByStudent(studentId);
        authenticationService.deleteUser(student.getEmail());
        locationService.deleteLocationsByStudentId(studentId);
        identificationService.deleteIdentificationById(student.getIdentification().getId());
        studentRepository.deleteById(studentId);

        //TODO: must remove the student image

    }
}
