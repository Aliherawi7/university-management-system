package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.IdentificationDTO;
import com.mycompany.umsapi.models.hrms.Identification;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.repositories.IdentificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentificationService {
    private final IdentificationRepository identificationRepository;

    public void addIdentification(Identification identification){
        identificationRepository.save(identification);
    }

    public Identification addIdentificationDTO(IdentificationDTO identificationDTO){
        Identification identification = Identification.builder()
                .pageNumber(identificationDTO.pageNumber())
                .caseNumber(identificationDTO.caseNumber())
                .nationalId(identificationDTO.nationalId())
                .registrationNumber(identificationDTO.registrationNumber())
                .build();
        addIdentification(identification);
        return identification;
    }

    public void updateStudentIdentification(Student student, IdentificationDTO identificationDTO){
        Identification identification = student.getIdentification();
        identification.setCaseNumber(identificationDTO.caseNumber());
        identification.setNationalId(identificationDTO.nationalId());
        identification.setPageNumber(identificationDTO.pageNumber());
        identification.setRegistrationNumber(identificationDTO.registrationNumber());
        addIdentification(identification);
    }

    public boolean isNationalIdAlreadyExist(Long nationalId) {
        return identificationRepository.existsByNationalId(nationalId);
    }

    public void deleteIdentificationById(Long id){
        identificationRepository.deleteById(id);
    }

}
