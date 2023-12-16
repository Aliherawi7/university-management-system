package com.mycompany.portalapi.constants;

public enum ExamTypes {
    MIDTERM ("MIDTERM"),
    FINAL("FINAL");

    private final String value;

    ExamTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
