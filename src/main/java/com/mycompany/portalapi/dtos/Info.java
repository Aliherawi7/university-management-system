package com.mycompany.portalapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.MaritalStatusDeserializer;
import com.mycompany.portalapi.constants.MaritalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {
    String name;
    String lastName;
    String fatherName;
    String grandFatherName;
    String dob;
    String motherTongue;
    MaritalStatus maritalStatus;

}
