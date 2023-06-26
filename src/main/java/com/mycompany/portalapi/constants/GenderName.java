package com.mycompany.portalapi.constants;

public enum GenderName {
    MALE("مرد"),
    FEMALE("زن"),
    OTHER("دیگر");

    private final String value;

    GenderName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
