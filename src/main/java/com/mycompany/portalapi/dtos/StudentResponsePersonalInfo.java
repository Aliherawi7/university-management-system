package com.mycompany.portalapi.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.MaritalStatusDeserializer;
import com.mycompany.portalapi.constants.MaritalStatus;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record StudentResponsePersonalInfo(@NonNull
                                         String name,
                                          @NonNull
                                         String lastName,
                                          @NonNull
                                         String fatherName,
                                          @NonNull
                                         String grandFatherName,
                                          @NonNull
                                         String dob,
                                          @NonNull
                                         String motherTongue,
                                          @NonNull
                                         @JsonDeserialize(using = MaritalStatusDeserializer.class)
                                         MaritalStatus maritalStatus,
                                          @NonNull
                                         String highSchool,
                                          @NonNull
                                         String schoolGraduationDate,
                                          @NonNull
                                         String fieldOfStudy,
                                          @NonNull
                                         String department) {
}
