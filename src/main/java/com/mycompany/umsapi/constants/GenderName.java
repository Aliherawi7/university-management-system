package com.mycompany.umsapi.constants;

public enum GenderName {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private final String value;

    GenderName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
