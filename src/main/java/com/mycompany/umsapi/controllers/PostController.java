package com.mycompany.umsapi.controllers;


import com.mycompany.umsapi.dtos.APIResponse;
import com.mycompany.umsapi.dtos.postDto.PostRequestDTO;
import com.mycompany.umsapi.dtos.postDto.PostSuccessfulRegistrationDTO;
import com.mycompany.umsapi.services.GeneralPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final GeneralPostService generalPostService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody PostRequestDTO postRequestDTO){
        return new ResponseEntity<>(generalPostService.addPost(postRequestDTO), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllPosts(@PathVariable Long id){
        return ResponseEntity.ok(generalPostService.getPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostSuccessfulRegistrationDTO> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO postRequestDTO){
        return ResponseEntity.ok(generalPostService.updatePost(id, postRequestDTO));
    }

    @GetMapping( "/")
    public ResponseEntity<?> getAllPosts(@RequestParam(name = "offset") int offset,
                                         @RequestParam(name = "pageSize") int pageSize){
        return ResponseEntity.ok(generalPostService.getPostAllPostByPagination(offset, pageSize));
    }

    @GetMapping("/{id}/hide-show")
    public ResponseEntity<?> toggleHideShowPost(@PathVariable Long id){
        generalPostService.toggleHideShowPost(id);
        return ResponseEntity.ok("پست موفقانه بروزرسانی شد!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        generalPostService.deletePost(id);
        return ResponseEntity.ok(APIResponse.builder()
                        .message("پست با موفقیت حذف شد!")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .build());
    }

}
