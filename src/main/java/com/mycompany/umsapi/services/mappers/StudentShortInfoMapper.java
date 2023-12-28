package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.studentDto.StudentShortInfo;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.utils.BaseURI;
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
                .faculty(student.getDepartment().getFaculty().getFacultyName())
                .department(student.getDepartment().getDepartmentName())
                .id(student.getId())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId()+".png").build();
    }
}
