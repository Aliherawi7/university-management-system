package com.mycompany.umsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.umsapi.constants.*;
import com.mycompany.umsapi.dtos.facultyDto.FacultyDTO;
import com.mycompany.umsapi.dtos.facultyDto.SemesterDTO;
import com.mycompany.umsapi.dtos.studentDto.StudentRegistrationDTO;
import com.mycompany.umsapi.models.attendance.AttendanceStatus;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.hrms.*;
import com.mycompany.umsapi.repositories.*;
import com.mycompany.umsapi.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class UMSApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UMSApiApplication.class, args);
    }



    @Bean
    CommandLineRunner run(StudentService studentService,
                          DepartmentRepository departmentRepository,
                          SemesterService semesterService,
                          FacultyService facultyService,
                          RoleRepository roleRepository,
                          GenderRepository genderRepository,
                          MaritalStatusRepository maritalStatusRepository,
                          RelationshipRepository relationshipRepository,
                          UserService userService,
                          GeneralPostService generalPostService,
                          SubjectService subjectService,
                          AttendanceStatusRepository attendanceStatusRepository,
                          UserTypeRepository userTypeRepository) {
        return args -> {




            ObjectMapper objectMapper = new ObjectMapper();
            Role supAdminRole = Role.builder().id(1).roleName(RoleName.SUPER_ADMIN).build();
            Role adminRole = Role.builder().id(2).roleName(RoleName.ADMIN).build();
            Role teacherRole = Role.builder().id(3).roleName(RoleName.TEACHER).build();
            Role employeeRole = Role.builder().id(4).roleName(RoleName.EMPLOYEE).build();
            Role studentRole = Role.builder().id(5).roleName(RoleName.STUDENT).build();
            roleRepository.save(supAdminRole);
            roleRepository.save(adminRole);
            roleRepository.save(teacherRole);
            roleRepository.save(employeeRole);
            roleRepository.save(studentRole);


            Gender gender1 = Gender.builder().id(1).name(GenderName.MALE.getValue()).build();
            Gender gender2 = Gender.builder().id(2).name(GenderName.FEMALE.getValue()).build();
            Gender gender3 = Gender.builder().id(3).name(GenderName.OTHER.getValue()).build();
            genderRepository.save(gender1);
            genderRepository.save(gender2);
            genderRepository.save(gender3);
            /* marital status */
            MaritalStatus maritalStatus1 = MaritalStatus.builder().id(1).name(MaritalStatusName.MARRIED.getValue()).build();
            MaritalStatus maritalStatus2 = MaritalStatus.builder().id(2).name(MaritalStatusName.SINGLE.getValue()).build();
            maritalStatusRepository.save(maritalStatus1);
            maritalStatusRepository.save(maritalStatus2);

            UserType ut1 = UserType.builder().userTypeName(UserTypeName.OWNER).id(1).build();
            UserType ut2 = UserType.builder().userTypeName(UserTypeName.STAFF).id(2).build();
            UserType ut3 = UserType.builder().userTypeName(UserTypeName.TEACHER).id(3).build();
            UserType ut4 = UserType.builder().userTypeName(UserTypeName.STUDENT).id(4).build();
            userTypeRepository.save(ut1);
            userTypeRepository.save(ut2);
            userTypeRepository.save(ut3);
            userTypeRepository.save(ut4);


            /* add all field of studies from the json file */
//            URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/fieldOfStudy.json");
//            FacultyDTO[] facultyDTO = null;
//            try {
//                facultyDTO = objectMapper.readValue(jsonUrl, FacultyDTO[].class);
//                ArrayList<FacultyDTO> fList = new ArrayList<>(Arrays.asList(facultyDTO));
//                fList.forEach(facultyService::addFaculty);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            /* add all field of studies from the json file */
//            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/department.json");
//            Department[] departmentDTOs = null;
//            try {
//                departmentDTOs = objectMapper.readValue(jsonUrl, Department[].class);
//                ArrayList<Department> dList = new ArrayList<>(Arrays.asList(departmentDTOs));
//                departmentRepository.saveAll(dList);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/semester.json");
//            SemesterDTO[] semesterDTOS = null;
//            try {
//                semesterDTOS = objectMapper.readValue(jsonUrl, SemesterDTO[].class);
//                ArrayList<SemesterDTO> dList = new ArrayList<>(Arrays.asList(semesterDTOS));
//                dList.forEach(semesterService::addSemester);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            /* attendance status*/
            AttendanceStatus absent = AttendanceStatus.builder().id(1).attendanceStatusName(AttendanceStatusName.ABSENT).build();
            AttendanceStatus present = AttendanceStatus.builder().id(2).attendanceStatusName(AttendanceStatusName.PRESENT).build();
            AttendanceStatus unknown = AttendanceStatus.builder().id(3).attendanceStatusName(AttendanceStatusName.UNKNOWN).build();
            attendanceStatusRepository.save(absent);
            attendanceStatusRepository.save(present);
            attendanceStatusRepository.save(unknown);

            /*adding the admin user to the db*/
            UserApp userApp = UserApp.builder()
                    .name("Ali Ahmad")
                    .id(0L)
                    .email("aliherawi7@gmail.com")
                    .lastname("Herawi")
                    .roles(Set.of(supAdminRole))
                    .genderName(gender1)
                    .userType(ut1)
                    .isEnabled(true)
                    .password("admin")
                    .build();
            userService.registerUser(userApp);

            /* relationships */
            Relationship father = Relationship.builder().id(1).relationName(RelationName.FATHER).build();
            relationshipRepository.save(father);
            Relationship brother = Relationship.builder().id(2).relationName(RelationName.BROTHER).build();
            relationshipRepository.save(brother);
            Relationship husband = Relationship.builder().id(3).relationName(RelationName.HUSBAND).build();
            relationshipRepository.save(husband);
            Relationship uncle = Relationship.builder().id(4).relationName(RelationName.PATERNAL_UNCLE).build();
            relationshipRepository.save(uncle);
            Relationship aunt = Relationship.builder().id(5).relationName(RelationName.MATERNAL_UNCLE).build();
            relationshipRepository.save(aunt);
            URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/student.json");
            StudentRegistrationDTO[] students = null;
//            try {
//                students = objectMapper.readValue(jsonUrl, StudentRegistrationDTO[].class);
//
//                Arrays.asList(students).forEach(student -> {
//                    studentService.addStudent(student);
//                });
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            /* add all posts from the json file */
//            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/posts.json");
//            PostRequestDTO[] postRequestDTOS = null;
//            try {
//                postRequestDTOS = objectMapper.readValue(jsonUrl, PostRequestDTO[].class);
//                ArrayList<PostRequestDTO> dList = new ArrayList<>(Arrays.asList(postRequestDTOS));
//                dList.forEach(generalPostService::addRawPost);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }


            // save all the subjects
//            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/subject.json");
//            Subject[] subjects = null;
//            try {
//                subjects = objectMapper.readValue(jsonUrl, Subject[].class);
//                ArrayList<Subject> dList = new ArrayList<>(Arrays.asList(subjects));
//                dList.forEach(sub -> {
//                    sub.setId(null);
//                    subjectService.addSubject(sub);
//                });
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

        };
    }


}
