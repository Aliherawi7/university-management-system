package com.mycompany.umsapi.constants;

public enum RelationName {
    FATHER("FATHER"),
    BROTHER("BROTHER"),
    PATERNAL_UNCLE("PATERNAL-UNCLE"),
    MATERNAL_UNCLE("MATERNAL-UNCLE"),
    HUSBAND("HUSBAND");

    private final String value;

    RelationName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
