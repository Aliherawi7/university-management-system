package com.mycompany.portalapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.MaritalStatusDeserializer;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = MaritalStatusDeserializer.class)
public enum MaritalStatus {
    MARRIED("MARRIED"),
    SINGLE("SINGLE");

    private final String value;

    MaritalStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static MaritalStatus decode(final String value){
        return Stream.of(MaritalStatus.values()).filter(targetEnum -> targetEnum.getValue().equals(value.toUpperCase())).findFirst().orElse(null);
    }

}
