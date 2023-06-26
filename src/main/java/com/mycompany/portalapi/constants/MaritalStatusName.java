package com.mycompany.portalapi.constants;

public enum MaritalStatusName {
    MARRIED("متاهل"),
    SINGLE("مجرد");

    private final String value;

    MaritalStatusName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
