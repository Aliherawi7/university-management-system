package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.studentDto.RelativeRegistrationDTO;
import com.mycompany.umsapi.models.hrms.Relative;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RelativeRegistrationDTOMapper implements Function<Relative, RelativeRegistrationDTO> {
    @Override
    public RelativeRegistrationDTO apply(Relative relative) {
        return RelativeRegistrationDTO
                .builder()
                .name(relative.getName())
                .job(relative.getJob())
                .phoneNumber(relative.getPhoneNumber())
                .jobLocation(relative.getJobLocation())
                .relationshipId(relative.getRelationship().getId())
                .build();
    }
}
