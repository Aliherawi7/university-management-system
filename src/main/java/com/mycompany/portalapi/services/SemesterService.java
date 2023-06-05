package com.mycompany.portalapi.services;

import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Semester;
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

    public Semester getSemesterBySemesterAndFieldOfStudyAndDepartment(Long semester, String fieldOfStudy, String department){
        return semesterRepository.findBySemesterAndFieldOfStudyAndDepartment(semester, fieldOfStudy, department)
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
