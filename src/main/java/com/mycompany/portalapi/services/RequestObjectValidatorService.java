package com.mycompany.portalapi.services;


import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestObjectValidatorService<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public void validate(T objectToValidate){
        Set<ConstraintViolation<T>> validation = validator.validate(objectToValidate);
        System.out.println(validation);
        if(!validation.isEmpty()){
            String messages = validation.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(" | "));
            System.out.println("messages: "+messages);
            throw new IllegalArgumentException(messages);
        }
    }


}
