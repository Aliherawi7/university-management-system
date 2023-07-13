package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByFieldOfStudyAndDepartmentOrderByDateTimeDesc(String fieldOfStudy, String department, Pageable pageable);
    Page<Post> findAllBySemesterOrderByDateTimeDesc(int semester, Pageable pageable);
    Page<Post> findAllByFieldOfStudyOrderByDateTimeDesc(String fieldOfStudy, Pageable pageable);
    Page<Post> findAllBySemesterAndFieldOfStudyOrderByDateTimeDesc(int semester,String fieldOfStudy, Pageable pageable);
    Page<Post> findAllBySemesterAndFieldOfStudyAndDepartmentOrderByDateTimeDesc(int semester,String fieldOfStudy,String department, Pageable pageable);
}
