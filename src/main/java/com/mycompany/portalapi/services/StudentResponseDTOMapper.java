package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.IdentificationDTO;
import com.mycompany.portalapi.dtos.StudentPersonalInfo;
import com.mycompany.portalapi.dtos.StudentResponseDTO;
import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.utils.BaseURI;
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
    private final DepartmentService departmentService;
    private final FieldOfStudyService fieldOfStudyService;
    private final IdentificationDTOMapper identificationDTOMapper;

    @Override
    public StudentResponseDTO apply(Student student) {
        StudentPersonalInfo studentPersonalInfo = StudentPersonalInfo
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
