package com.mycompany.portalapi.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mycompany.portalapi.constants.Gender;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Gender> {
    @Override
    public Gender deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        if(value != null){
            try {
                return Gender.valueOf(value.toUpperCase());
            }catch (IllegalArgumentException ex){
                throw new JsonParseException("Invalid Gender parameter");
            }
        }
        return null;
    }
}
