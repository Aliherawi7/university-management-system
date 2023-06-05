package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.StudentResponseDTO;
import com.mycompany.portalapi.dtos.StudentResponsePersonalInfo;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.utils.BaseURI;
import com.mycompany.portalapi.utils.StudentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@AllArgsConstructor
@Component
public class StudentResponseDTOMapper implements Function<Student, StudentResponseDTO> {
    private final HttpServletRequest httpServletRequest;
    private final IdentificationService identificationService;
    private final LocationService locationService;
    private final RelativeService relativeService;
    private final IdentificationDTOMapper identificationDTOMapper;

    @Override
    public StudentResponseDTO apply(Student student) {
                StudentResponsePersonalInfo studentPersonalInfo = StudentResponsePersonalInfo
                .builder()
                .name(student.getName())
                .dob(student.getDob().toString())
                .fatherName(student.getFatherName())
                .lastName(student.getLastName())
                .grandFatherName(student.getGrandFatherName())
                .motherTongue(student.getMotherTongue())
                .maritalStatus(student.getMaritalStatus())
                .schoolGraduationDate(student.getSchoolGraduationDate().toString())
                .department(student.getDepartment())
                .fieldOfStudy(student.getFieldOfStudy())
                .highSchool(student.getHighSchool())
                .phoneNumber(student.getPhoneNumber())
                .semester(student.getSemester())
                .year(StudentUtils.getYear(student.getSemester()))
                .build();
        return StudentResponseDTO
                .builder()
                .studentPersonalInfo(studentPersonalInfo)
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId())
                .locations(locationService.getCurrentAndPreviousLocationsOfStudent(student.getId()))
                .relatives(relativeService.getAllStudentRelativesById(student.getId()))
                .identification(identificationDTOMapper
                        .apply(identificationService.getIdentificationByStudentId(student.getId())))
                .build();
    }




}
