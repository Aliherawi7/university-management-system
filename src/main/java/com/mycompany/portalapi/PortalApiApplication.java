package com.mycompany.portalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.DepartmentDTO;
import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.models.Role;
import com.mycompany.portalapi.repositories.RoleRepository;
import com.mycompany.portalapi.services.DepartmentService;
import com.mycompany.portalapi.services.FieldOfStudyService;
import com.mycompany.portalapi.services.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class PortalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApiApplication.class, args);
    }

    @Bean
    CommandLineRunner run(StudentService studentService,
                          DepartmentService departmentService,
                          FieldOfStudyService fieldOfStudyService,
                          RoleRepository roleRepository) {
        return args -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Role adminRole = Role.builder().id(1).roleName(RoleName.ADMIN).build();
            Role studentRole = Role.builder().id(2).roleName(RoleName.STUDENT).build();
            roleRepository.save(adminRole);
            roleRepository.save(studentRole);

            URL jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json\\student.json");
            StudentRegistrationDTO student = null;
            try {
                student = objectMapper.readValue(jsonUrl, StudentRegistrationDTO.class);

                studentService.addStudent(student);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /* add all field of studies from the json file */
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json\\fieldOfStudy.json");
            FieldOfStudyDTO[] fieldOfStudyDTO = null;
            try {
                fieldOfStudyDTO = objectMapper.readValue(jsonUrl, FieldOfStudyDTO[].class);
                ArrayList<FieldOfStudyDTO> fList = new ArrayList<>(Arrays.asList(fieldOfStudyDTO));
                fList.forEach(fieldOfStudyService::addFieldOfStudy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /* add all field of studies from the json file */
            jsonUrl = Thread.currentThread().getContextClassLoader().getResource("json\\department.json");
            DepartmentDTO[] departmentDTOs = null;
            try {
                departmentDTOs = objectMapper.readValue(jsonUrl, DepartmentDTO[].class);
                ArrayList<DepartmentDTO> dList = new ArrayList<>(Arrays.asList(departmentDTOs));
                dList.forEach(departmentService::addDepartment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };
    }

}
