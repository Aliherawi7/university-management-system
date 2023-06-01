package com.mycompany.portalapi.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.constants.MaritalStatus;
import com.mycompany.portalapi.constants.Relation;

import java.io.IOException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = RelationDeserializer.class)
public class RelationDeserializer extends JsonDeserializer<Relation> {
    @Override
    public Relation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        if(value != null){
            try {
                return Relation.valueOf(value.toUpperCase());
            }catch (IllegalArgumentException ex){
                throw new JsonParseException("Invalid Gender parameter");
            }
        }
        return null;
    }
}
