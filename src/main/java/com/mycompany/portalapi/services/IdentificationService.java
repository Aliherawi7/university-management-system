package com.mycompany.portalapi.services;

import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.repositories.IdentificationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentificationService {
    private final IdentificationRepository identificationRepository;

    public void addIdentification(Identification identification){
        identificationRepository.save(identification);
    }
    public Identification getIdentificationByStudentId(Long studentId){
        return identificationRepository.findByStudentId(studentId);
    }
    public boolean isNationalIdAlreadyExist(Long nationalId) {
        return identificationRepository.existsByNationalId(nationalId);
    }

    public void deleteIdentificationByStudent(Long studentId){
        Identification identificationByStudentId = getIdentificationByStudentId(studentId);
        identificationRepository.delete(identificationByStudentId);
    }

}
