package com.mycompany.portalapi.constants;

public enum AttendanceStatusName {
    PRESENT("PRESENT"),
    ABSENT("ABSENT"),
    UNKNOWN("UNKNOWN");

    private final String value;

    AttendanceStatusName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
