package com.mycompany.portalapi.services;

import com.mycompany.portalapi.models.hrms.Identification;
import com.mycompany.portalapi.repositories.IdentificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentificationService {
    private final IdentificationRepository identificationRepository;

    public void addIdentification(Identification identification){
        identificationRepository.save(identification);
    }

    public boolean isNationalIdAlreadyExist(Long nationalId) {
        return identificationRepository.existsByNationalId(nationalId);
    }

    public void deleteIdentificationById(Long id){
        identificationRepository.deleteById(id);
    }

}
