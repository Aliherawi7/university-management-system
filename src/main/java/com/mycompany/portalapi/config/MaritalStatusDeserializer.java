package com.mycompany.portalapi.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mycompany.portalapi.constants.Gender;
import com.mycompany.portalapi.constants.MaritalStatus;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;

import java.io.IOException;

public class MaritalStatusDeserializer extends JsonDeserializer<MaritalStatus> {
    @Override
    public MaritalStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        if(value != null){
            try {
                return MaritalStatus.valueOf(value.toUpperCase());
            }catch (IllegalArgumentException ex){
                throw new IllegalArgumentException("Invalid Gender parameter");
            }
        }
        return null;
    }
}
