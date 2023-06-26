package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByFieldOfStudyAndDepartment(String fieldOfStudy, String department);

}