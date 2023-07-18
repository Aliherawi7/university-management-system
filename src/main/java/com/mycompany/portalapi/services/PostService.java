package com.mycompany.portalapi.services;


import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.PageContainerDTO;
import com.mycompany.portalapi.dtos.PostRequestDTO;
import com.mycompany.portalapi.dtos.PostResponseDTO;
import com.mycompany.portalapi.dtos.PostSuccessfulRegistrationDTO;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.repositories.PostRepository;
import com.mycompany.portalapi.repositories.StudentRepository;
import com.mycompany.portalapi.security.JwtUtils;
import com.mycompany.portalapi.services.mappers.PostResponseDTOMapper;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
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

    public PostSuccessfulRegistrationDTO addPost(PostRequestDTO postRequestDTO) {
        Post post = addRawPost(postRequestDTO);
        return PostSuccessfulRegistrationDTO.builder()
                .message("post successfully saved")
                .postId(post.getId())
                .statusCode(HttpStatus.CREATED.value())
                .filesUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST.getValue() + post.getId())
                .build();
    }

    public Post addRawPost(PostRequestDTO postRequestDTO) {
        Post savedPost = Post.builder()
                .authorId(postRequestDTO.authorId())
                .dateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .semester(postRequestDTO.semester())
                .department(postRequestDTO.department())
                .fieldOfStudy(postRequestDTO.fieldOfStudy())
                .message(postRequestDTO.message())
                .isPublic(postRequestDTO.isPublic())
                .build();
        return postRepository.save(savedPost);
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

    /* this is method is for student client which is going to
    return post by this student information*/
    public Page<PostResponseDTO> getPostAllPostByPagination(int offset, int pageSize, HttpServletRequest httpServletRequest) {
        String email = jwtUtils.getUserEmailByJWT(jwtUtils.getToken(httpServletRequest).substring(7));
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token "));
        return postRepository.fetchAllPostByKeywordAndFieldOfStudyAndDepartmentAndSemester(
                student.getFieldOfStudy(),
                student.getDepartment(),
                student.getSemester(),
                PageRequest.of(offset, pageSize)
        ).map(postResponseDTOMapper);
    }

    public Page<PostResponseDTO> getAllPostsByRequestParams(Integer semester,
                                                            String fieldOfStudy,
                                                            String department,
                                                            int offset,
                                                            int pageSize) {
        Page<Post> posts = null;
        PageRequest pageRequest = PageRequest.of(offset, pageSize);
        if (semester != null) {
            posts = postRepository.fetchAllPostByKeywordAndFieldOfStudyAndDepartmentAndSemester(fieldOfStudy, department, semester, pageRequest);
        }else {
            posts = postRepository.fetchAllPostByKeywordAndFieldOfStudyAndDepartment(fieldOfStudy, department, pageRequest);
        }
        return posts.map(postResponseDTOMapper);
    }

    public boolean isExistById(Long id) {
        return postRepository.existsById(id);
    }

    public void checkIfNotExist(Long id) {
        if (!isExistById(id)) {
            throw new ResourceNotFoundException("پست مورد نظر یافت نشد!");
        }
    }

    public List<String> getAllPostFileUrl(Long postId) {
        return fileStorageService.getAllPostFileName(postId)
                .stream().map(name -> {
                    return BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST + name;
                }).toList();
    }

    /* update post */
    public void updatePost(PostRequestDTO postRequestDTO){

    }


}
