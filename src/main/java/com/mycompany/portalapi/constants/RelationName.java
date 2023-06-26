package com.mycompany.portalapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.stream.Stream;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Relation {
    FATHER("FATHER"),
    BROTHER("BROTHER"),
    UNCLE("UNCLE"),
    AUNT("AUNT"),
    HUSBAND("HUSBAND");

    private final String value;

    Relation(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static Relation decode(final String value){
        return Stream.of(Relation.values()).filter(targetEnum -> targetEnum.getValue().equals(value.toUpperCase())).findFirst().orElse(null);
    }
}
