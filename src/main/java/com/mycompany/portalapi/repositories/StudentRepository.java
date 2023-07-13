package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select s from Student s where function('LOCATE', function('COALESCE', :keyword, ''), function('CONCAT', s.name, ' ', s.lastName)) > 0 " +
            "and s.fieldOfStudy like function('COALESCE', :fieldOfStudy, '%') and s.department like function('COALESCE', :department, '%')")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartment(String keyword, String fieldOfStudy, String department, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE function('LOCATE', function('COALESCE', :keyword, ''), function('CONCAT', s.name, ' ', s.lastName)) > 0 " +
            "AND s.fieldOfStudy LIKE function('COALESCE', :fieldOfStudy, '%') " +
            "AND s.department LIKE function('COALESCE', :department, '%') " +
            "AND s.semester = :semester")
    Page<Student> fetchAllStudentByKeywordAndFieldOfStudyAndDepartmentAndSemester(@Param("keyword") String keyword,
                                                                                  @Param("fieldOfStudy") String fieldOfStudy,
                                                                                  @Param("department") String department,
                                                                                  @Param("semester") Integer semester,
                                                                                  Pageable pageable);
    Page<Student> findAllByName(String name, Pageable pageable);

    Page<Student> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
