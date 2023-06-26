package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.AuthorDTO;
import com.mycompany.portalapi.dtos.PageContainerDTO;
import com.mycompany.portalapi.dtos.PostResponseDTO;
import com.mycompany.portalapi.dtos.PostSuccessfulRegistrationDTO;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.repositories.PostRepository;
import com.mycompany.portalapi.repositories.StudentRepository;
import com.mycompany.portalapi.security.JwtUtils;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final HttpServletRequest httpServletRequest;
    private final FileStorageService fileStorageService;
    private final PostResponseDTOMapper postResponseDTOMapper;
    private final StudentRepository studentRepository;
    private final JwtUtils jwtUtils;

    public PostSuccessfulRegistrationDTO addPost(Post post) {
        post.setId(null);
        post.setDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
        Post savedPost = postRepository.save(post);
        return PostSuccessfulRegistrationDTO.builder()
                .message("post successfully saved")
                .postId(post.getId())
                .statusCode(HttpStatus.CREATED.value())
                .filesUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST.getValue() + post.getId())
                .build();
    }

    public void addPost(Post post, List<MultipartFile> files) {
        PostSuccessfulRegistrationDTO savedPost = addPost(post);
        files.forEach(file -> {
            log.info("file originalName:{} , file name:{}, file contentType: {}, file:{}", file.getOriginalFilename(), file.getName(), file.getContentType(), file);
        });
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with provided id: " + postId));
    }

    public PostResponseDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with provided id: " + postId));
        return postResponseDTOMapper.apply(post);
    }

    public PageContainerDTO<PostResponseDTO> getPostAllPostByPagination(int offset, int pageSize, HttpServletRequest httpServletRequest) {
        String email = jwtUtils.getUserEmailByJWT(jwtUtils.getToken(httpServletRequest).substring(7));
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token "));
        List<PostResponseDTO> posts = new java.util.ArrayList<>(postRepository.findAllByFieldOfStudyAndDepartment(student.getFieldOfStudy(), student.getDepartment())
                .stream().map(postResponseDTOMapper).toList());
        log.info("list {}", posts);
        posts.sort(Comparator.comparing(PostResponseDTO::dateTime));
        return new PageContainerDTO<>(
                posts.size(),
                posts
        );
    }


    public boolean isExistById(Long id) {
        return postRepository.existsById(id);
    }

    public void checkIfNotExist(Long id) {
        if (!isExistById(id)) {
            throw new ResourceNotFoundException("post not found with provided id: " + id);
        }
    }

    public List<String> getAllPostFileUrl(Long postId) {
        return fileStorageService.getAllPostFileName(postId)
                .stream().map(name -> {
                    return BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST + name;
                }).toList();
    }


}
