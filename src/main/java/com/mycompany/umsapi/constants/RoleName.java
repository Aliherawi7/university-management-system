package com.mycompany.umsapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum RoleName {
    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN"),
    TEACHER("TEACHER"),
    EMPLOYEE("EMPLOYEE"),
    STUDENT("STUDENT");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    @JsonCreator
    public static RoleName decode(final String value){
        return Stream.of(RoleName.values()).filter(targetEnum -> targetEnum.getValue().equals(value.toUpperCase())).findFirst().orElse(null);
    }
}
