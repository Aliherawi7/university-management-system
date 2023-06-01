package com.mycompany.portalapi.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.MaritalStatusDeserializer;
import com.mycompany.portalapi.config.RelationDeserializer;
import com.mycompany.portalapi.constants.MaritalStatus;
import lombok.Builder;

@Builder
public record StudentPersonalInfo(
        String name,
        String lastName,
        String fatherName,
        String grandFatherName,
        String dob,
        String motherTongue,
        @JsonDeserialize(using = MaritalStatusDeserializer.class)
        MaritalStatus maritalStatus,
        String highSchool,
        String schoolGraduationDate,
        String fieldOfStudy,
        String department
) {
}
