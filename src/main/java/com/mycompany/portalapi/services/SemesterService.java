package com.mycompany.portalapi.services;

import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.repositories.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;


    public void addSemester(Semester semester){
        // do check

        semesterRepository.save(semester);
    }

    public Semester getSemesterBySemesterAndFieldOfStudyAndDepartment(Long semester, Long department){
        return semesterRepository.findBySemesterAndDepartment_Id(semester, department)
                .orElseThrow(() -> new ResourceNotFoundException("semester not found with provided information"));
    }

    public Semester getSemesterById(Long semesterId){
        return semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("semester not found with provided id: "+semesterId));
    }

    public void updateSemester(Semester semester){
        semesterRepository.save(semester);
    }

    public void deleteSemester(Long id){
        semesterRepository.deleteById(id);
    }

}
