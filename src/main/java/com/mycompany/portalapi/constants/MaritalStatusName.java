package com.mycompany.portalapi.constants;

public enum MaritalStatusName {
    MARRIED("MARRIED"),
    SINGLE("SINGLE");

    private final String value;

    MaritalStatusName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
