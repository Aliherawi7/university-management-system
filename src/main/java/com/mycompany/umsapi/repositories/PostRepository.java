package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.GeneralPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<GeneralPost, Long> {
    @Query("select p from GeneralPost p order by p.dateTime desc ")
    Page<GeneralPost> fetchAllPosts(Pageable pageable);

}
