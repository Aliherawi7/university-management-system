package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.IdentificationDTO;
import com.mycompany.umsapi.dtos.studentDto.StudentResponseDTO;
import com.mycompany.umsapi.dtos.studentDto.StudentResponsePersonalInfo;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.services.UserService;
import com.mycompany.umsapi.utils.BaseURI;
import com.mycompany.umsapi.utils.StudentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StudentResponseDTOMapper implements Function<Student, StudentResponseDTO> {
    private final HttpServletRequest httpServletRequest;
    private final RelativeRegistrationDTOMapper relativeRegistrationDTOMapper;
    private final LocationDTOMapper locationDTOMapper;
    private final UserService userService;

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
                .gender(student.getGender().getName())
                .maritalStatus(student.getMaritalStatus().getName())
                .schoolGraduationDate(student.getSchoolGraduationDate().toString())
                .department(student.getDepartment().getDepartmentName())
                .faculty(student.getDepartment().getFaculty().getFacultyName())
                .highSchool(student.getHighSchool())
                .phoneNumber(student.getPhoneNumber())
                .email(student.getEmail())
                .joinedDate(student.getJoinedDate())
                .semester(student.getSemester().getSemester())
                .year(StudentUtils.getYear(student.getSemester().getSemester()))
                .build();
        IdentificationDTO identificationDTO = IdentificationDTO
                .builder()
                .nationalId(student.getIdentification().getNationalId())
                .pageNumber(student.getIdentification().getPageNumber())
                .registrationNumber(student.getIdentification().getRegistrationNumber())
                .caseNumber(student.getIdentification().getCaseNumber())
                .build();
        return StudentResponseDTO
                .builder()
                .studentPersonalInfo(studentPersonalInfo)
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + student.getId()+".png")
                .isEnable(userService.isEnable(student.getEmail()))
                .locations(student.getLocations().stream().map(locationDTOMapper).toList())
                .relatives(student.getRelatives().stream().map(relativeRegistrationDTOMapper).toList())
                .identification(identificationDTO)
                .build();
    }


}
