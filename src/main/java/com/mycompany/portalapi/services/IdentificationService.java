package com.mycompany.portalapi.services;

import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.repositories.IdentificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IdentificationService {
    private IdentificationRepository identificationRepository;

    public void addIdentification(Identification identification){
        identificationRepository.save(identification);
    }
    public Identification getIdentificationByStudentId(Long studentId){
        return identificationRepository.findByStudentId(studentId);
    }

}
