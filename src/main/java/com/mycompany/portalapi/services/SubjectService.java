package com.mycompany.portalapi.services;

import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;

    public void addSubject(Subject subject){
        subjectRepository.save(subject);
    }
    public Subject getSubjectById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with provided id: "+id));
    }

    /* find by something else */
    public List<Subject> search(String department){
        Department d = departmentService.getDepartmentByName(department);
        return subjectRepository.findAllByDepartmentsContaining(d);
    }
    public List<Subject> search(String department, Integer semester){
        Department d = departmentService.getDepartmentByName(department);
        Semester s = d.getSemesters()
                .stream()
                .filter(item -> Objects.equals(item.getSemester(), semester))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("semester not found!"));
        return subjectRepository.findAllByDepartmentsContainingAndSemesterContaining(d, s);
    }
    public List<Subject> searchByName(String name){
        return subjectRepository.findAllByName(name);
    }

    public void deleteSubjectById(Long id){
        subjectRepository.deleteById(id);
    }
}
