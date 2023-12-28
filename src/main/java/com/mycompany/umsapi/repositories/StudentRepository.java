package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.hrms.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select s from Student s " +
            "where" +
            "   (COALESCE(:keyword, '') = '' OR " +
            "   FUNCTION('LOCATE', CONCAT(s.name, ' ', s.lastName), :keyword) > 0) " +
            "   AND (:department IS NULL OR s.department.id = :department) ")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(String keyword, Long department, Pageable pageable);

    @Query("SELECT s FROM Student s " +
            "WHERE " +
            "   (COALESCE(:keyword, '') = '' OR " +
            "   FUNCTION('LOCATE', CONCAT(s.name, ' ', s.lastName), :keyword) > 0) " +
            "   AND (:department IS NULL OR s.department.id = :department) " +
            "   AND s.semester = :semester")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartmentAndSemester(
            @Param("keyword") String keyword,
            @Param("department") Long department,
            @Param("semester") Long semester,
            Pageable pageable);


    Page<Student> findAllByName(String name, Pageable pageable);

    Page<Student> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
