package com.mycompany.portalapi.services.mappers;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.StudentShortInfo;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StudentShortInfoMapper implements Function<Student, StudentShortInfo> {
    private final HttpServletRequest httpServletRequest;
    @Override
    public StudentShortInfo apply(Student student) {
        return StudentShortInfo.builder()
                .name(student.getName())
                .lastname(student.getLastName())
                .fatherName(student.getFatherName())
                .fieldStudy(student.getFieldOfStudy())
                .department(student.getDepartment())
                .id(student.getId())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId()+".png").build();
    }
}
