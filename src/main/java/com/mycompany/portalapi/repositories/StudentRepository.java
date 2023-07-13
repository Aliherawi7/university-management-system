package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) > 0")
    Page<Student> fetchAllStudentByKeyword(String keyword, Pageable pageable);

    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) >0 and s.fieldOfStudy = :fieldOfStudy")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudy(String keyword,String fieldOfStudy, Pageable pageable);
    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) > 0 and s.fieldOfStudy = :fieldOfStudy and s.department = :department")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(String keyword,String fieldOfStudy,String department, Pageable pageable);
    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) > 0 and s.fieldOfStudy = :fieldOfStudy and s.department = :department and s.semester = :semester")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartmentAndSemester(String keyword,String fieldOfStudy,String department, Integer semester, Pageable pageable);
    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) > 0 and s.fieldOfStudy = :fieldOfStudy and s.semester = :semester")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndSemester(String keyword, String fieldOfStudy, Integer semester, Pageable pageable);
    @Query("select s from Student s where function('LOCATE',:keyword, function('CONCAT', s.name, ' ', s.lastName)) > 0 and s.semester = :semester")
    Page<Student> fetchAllStudentByKeywordAndSemester(String keyword, Integer semester, Pageable pageable);

    Page<Student> findAllByName(String name, Pageable pageable);

    Page<Student> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
