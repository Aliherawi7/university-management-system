package com.mycompany.portalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.portalapi.constants.*;
import com.mycompany.portalapi.dtos.DepartmentDTO;
import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.dtos.postDto.PostRequestDTO;
import com.mycompany.portalapi.dtos.studentDto.StudentRegistrationDTO;
import com.mycompany.portalapi.models.attendance.AttendanceStatus;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.models.hrms.*;
import com.mycompany.portalapi.repositories.*;
import com.mycompany.portalapi.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class PortalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApiApplication.class, args);
    }




    CommandLineRunner run(StudentService studentService,
                          DepartmentService departmentService,
                          FacultyService facultyService,
                          RoleRepository roleRepository,
                          GenderRepository genderRepository,
                          MaritalStatusRepository maritalStatusRepository,
                          RelationshipRepository relationshipRepository,
                          AuthenticationService authenticationService,
                          GeneralPostService generalPostService,
                          SubjectService subjectService,
                          AttendanceStatusRepository attendanceStatusRepository) {
        return args -> {



            ObjectMapper objectMapper = new ObjectMapper();
            Role adminRole = Role.builder().id(1).roleName(RoleName.ADMIN).build();
            Role studentRole = Role.builder().id(2).roleName(RoleName.STUDENT).build();
            roleRepository.save(adminRole);
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


            /* attendance status*/
            AttendanceStatus absent = AttendanceStatus.builder().id(1).name(AttendanceStatusName.ABSENT.getValue()).build();
            AttendanceStatus present = AttendanceStatus.builder().id(2).name(AttendanceStatusName.PRESENT.getValue()).build();
            AttendanceStatus unknown = AttendanceStatus.builder().id(3).name(AttendanceStatusName.UNKNOWN.getValue()).build();
            attendanceStatusRepository.save(absent);
            attendanceStatusRepository.save(present);
            attendanceStatusRepository.save(unknown);

            /*adding the admin user to the db*/
            UserApp userApp = UserApp.builder()
                    .name("مدیر")
                    .id(0L)
                    .email("admin@gmail.com")
                    .lastname("عمومی")
                    .roles(Set.of(adminRole))
                    .genderName(gender1)
                    .isEnabled(true)
                    .password("admin")
                    .build();
            authenticationService.registerUser(userApp);

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
            try {
                students = objectMapper.readValue(jsonUrl, StudentRegistrationDTO[].class);

                Arrays.asList(students).forEach(student -> {
                    studentService.addStudent(student);
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /* add all field of studies from the json file */
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/fieldOfStudy.json");
            FieldOfStudyDTO[] fieldOfStudyDTO = null;
            try {
                fieldOfStudyDTO = objectMapper.readValue(jsonUrl, FieldOfStudyDTO[].class);
                ArrayList<FieldOfStudyDTO> fList = new ArrayList<>(Arrays.asList(fieldOfStudyDTO));
                fList.forEach(facultyService::addFieldOfStudy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /* add all field of studies from the json file */
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/department.json");
            DepartmentDTO[] departmentDTOs = null;
            try {
                departmentDTOs = objectMapper.readValue(jsonUrl, DepartmentDTO[].class);
                ArrayList<DepartmentDTO> dList = new ArrayList<>(Arrays.asList(departmentDTOs));
                dList.forEach(departmentService::addDepartment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /* add all posts from the json file */
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/posts.json");
            PostRequestDTO[] postRequestDTOS = null;
            try {
                postRequestDTOS = objectMapper.readValue(jsonUrl, PostRequestDTO[].class);
                ArrayList<PostRequestDTO> dList = new ArrayList<>(Arrays.asList(postRequestDTOS));
                dList.forEach(generalPostService::addRawPost);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // save all the subjects
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json/subject.json");
            Subject[] subjects = null;
            try {
                subjects = objectMapper.readValue(jsonUrl, Subject[].class);
                ArrayList<Subject> dList = new ArrayList<>(Arrays.asList(subjects));
                dList.forEach(sub -> {
                    sub.setId(null);
                    subjectService.addSubject(sub);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };
    }


}
