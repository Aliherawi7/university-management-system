package com.mycompany.umsapi.constants;

public enum UserTypeName {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    OWNER("OWNER"),
    STAFF("STAFF");

    private final String value;

    UserTypeName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
