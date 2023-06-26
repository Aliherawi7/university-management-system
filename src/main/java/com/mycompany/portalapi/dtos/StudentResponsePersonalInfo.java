package com.mycompany.portalapi.dtos;

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
                                          String gender,
                                          @NonNull
                                          String maritalStatus,
                                          @NonNull
                                          String highSchool,
                                          @NonNull
                                          String schoolGraduationDate,
                                          @NonNull
                                          String fieldOfStudy,
                                          @NonNull
                                          String department,
                                          @NonNull
                                          String phoneNumber,
                                          @NonNull
                                          Integer semester,
                                          @NonNull
                                          Integer year) {
}
