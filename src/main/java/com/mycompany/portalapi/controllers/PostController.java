package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.PostRequestDTO;
import com.mycompany.portalapi.dtos.PostResponseDTO;
import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody PostRequestDTO postRequestDTO){
        return new ResponseEntity<>(postService.addPost(postRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/student")
    public ResponseEntity<?> getAllPosts(@RequestParam(name = "offset") int offset,
                                         @RequestParam(name = "pageSize") int pageSize,
                                         HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(postService.getPostAllPostByPagination(offset, pageSize, httpServletRequest));
    }

    @GetMapping( "/")
    public ResponseEntity<?> getAllPosts(@RequestParam(name = "semester", required = false) Integer semester,
                                         @RequestParam(name = "fieldOfStudy",required = false) String fieldOfStudy,
                                         @RequestParam(name = "department",required = false) String department,
                                         @RequestParam(name = "offset") int offset,
                                         @RequestParam(name = "pageSize") int pageSize){
        return ResponseEntity.ok(postService.getAllPostsByRequestParams(
                semester,fieldOfStudy, department, offset, pageSize
        ));
    }
}
