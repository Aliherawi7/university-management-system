package com.mycompany.umsapi.dtos.studentDto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record  StudentResponsePersonalInfo(
                                          String name,
                                          String lastName,
                                          String fatherName,
                                          String grandFatherName,
                                          String dob,
                                          String email,
                                          LocalDate joinedDate,
                                          String motherTongue,
                                          String gender,

                                          String maritalStatus,

                                          String highSchool,

                                          String schoolGraduationDate,

                                          String faculty,

                                           String department,

                                          String phoneNumber,

                                          Integer semester,

                                          Integer year) {
}
