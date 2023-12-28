package com.mycompany.umsapi.services;


import com.mycompany.umsapi.exceptions.IllegalArgumentException;
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

        if(!validation.isEmpty()){
            String messages = validation.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(" | "));
            throw new IllegalArgumentException(messages);
        }
    }


}
