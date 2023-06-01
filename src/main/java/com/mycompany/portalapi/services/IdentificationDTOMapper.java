package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.IdentificationDTO;
import com.mycompany.portalapi.models.Identification;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IdentificationDTOMapper implements Function<Identification, IdentificationDTO> {
    @Override
    public IdentificationDTO apply(Identification identification) {
        return IdentificationDTO
                .builder()
                .caseNumber(identification.getCaseNumber())
                .registrationNumber(identification.getRegistrationNumber())
                .pageNumber(identification.getPageNumber())
                .nationalId(identification.getNationalId())
                .build();
    }
}
