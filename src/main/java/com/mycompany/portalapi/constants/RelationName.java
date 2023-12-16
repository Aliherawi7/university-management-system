package com.mycompany.portalapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.stream.Stream;

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
