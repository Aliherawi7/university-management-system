package com.mycompany.portalapi.services;

import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Subject;
import com.mycompany.portalapi.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public void addSubject(Subject subject){
        subjectRepository.save(subject);
    }
    public Subject getSubjectById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with provided id: "+id));
    }

    /* find by something else */
    public List<Subject> search(String field, String department){
        return subjectRepository.findAllByFieldOfStudyAndDepartment(field, department);
    }
    public List<Subject> search(String field, String department, String semester){
        return subjectRepository.findAllByFieldOfStudyAndDepartmentAndSemester(field, department, Integer.parseInt(semester));
    }
    public List<Subject> searchByName(String name){
        return subjectRepository.findAllByName(name);
    }
    public List<Subject> searchByFieldName(String field){
        return subjectRepository.findAllByFieldOfStudy(field);
    }

    public void deleteSubjectById(Long id){
        subjectRepository.deleteById(id);
    }
}
