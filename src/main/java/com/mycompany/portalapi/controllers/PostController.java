package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody Post post){
        return new ResponseEntity<>(postService.addPost(post), HttpStatus.CREATED);
    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<?> getAllPosts(@PathVariable int offset, @PathVariable int pageSize, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(postService.getPostAllPostByPagination(offset, pageSize, httpServletRequest));
    }

}
