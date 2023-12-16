package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByDepartmentsContainingAndSemesterContaining(Department department, Semester semester);
    List<Subject> findAllByDepartmentsContaining(Department department);

    List<Subject> findAllByName(String name);

}
