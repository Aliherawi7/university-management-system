package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where " +
            "p.fieldOfStudy like function('COALESCE', :fieldOfStudy, '%') and p.department like function('COALESCE', :department, '%') order by p.dateTime desc")
    Page<Post> fetchAllPostByKeywordAndFieldOfStudyAndDepartment(String fieldOfStudy, String department, Pageable pageable);

    @Query("select p from Post p where " +
            "p.fieldOfStudy like function('COALESCE', :fieldOfStudy, '%') and p.department like function('COALESCE', :department, '%')" +
            " and p.semester = :semester order by p.dateTime desc ")
    Page<Post> fetchAllPostByKeywordAndFieldOfStudyAndDepartmentAndSemester(String fieldOfStudy, String department,Integer semester, Pageable pageable);

}
