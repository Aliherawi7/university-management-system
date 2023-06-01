package com.mycompany.portalapi.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.GenderDeserializer;

import java.util.stream.Stream;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = GenderDeserializer.class)
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static Gender decode(final String value){
        return Stream.of(Gender.values()).filter(targetEnum -> targetEnum.value.equals(value.toUpperCase())).findFirst().orElse(null);
    }
}
