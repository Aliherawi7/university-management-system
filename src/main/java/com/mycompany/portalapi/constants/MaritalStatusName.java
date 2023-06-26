package com.mycompany.portalapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.MaritalStatusDeserializer;

import java.util.stream.Stream;

public enum MaritalStatus {
    MARRIED("متاهل"),
    SINGLE("مجرد");

    private final String value;

    MaritalStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
