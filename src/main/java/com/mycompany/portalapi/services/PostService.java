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
        return getPostAllPostBySemesterAndFieldOfStudyAndDepartmentWithPagination(student.getSemester(),
                student.getFieldOfStudy(),
                student.getDepartment(),
                offset,
                pageSize);
    }

    public Page<PostResponseDTO> getAllPostsByRequestParams(Integer semester,
                                                            String fieldOfStudy,
                                                            String department,
                                                            int offset,
                                                            int pageSize) {
        Page<PostResponseDTO> posts = null;
        if (semester != null && fieldOfStudy != null && department != null) {
            posts = getPostAllPostBySemesterAndFieldOfStudyAndDepartmentWithPagination(
                    semester, fieldOfStudy, department, offset, pageSize
            );
        } else if (semester != null && fieldOfStudy != null) {
            posts = getPostAllPostBySemesterAndFieldOfStudyWithPagination(
                    semester, fieldOfStudy, offset, pageSize
            );
        } else if (fieldOfStudy != null && department != null) {
            posts = getPostAllPostByFieldOfStudyAndDepartmentWithPagination(
                    fieldOfStudy, department, offset, pageSize
            );
        } else if (semester != null) {
           posts = getPostAllPostBySemesterWithPagination(semester, offset, pageSize);
        }
        return posts;
    }

    /* get all posts by [semester, pagination] */
    public Page<PostResponseDTO> getPostAllPostBySemesterWithPagination(int semester, int offset, int pageSize) {
        Page<PostResponseDTO> posts = postRepository
                .findAllBySemesterOrderByDateTimeDesc(
                        semester,
                        PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
        log.info("list {}", posts);
        return posts;
    }
    /* get all posts by [fieldOfStudy, pagination] */
    public Page<PostResponseDTO> getPostAllPostByFieldOfStudyWithPagination(String fieldOfStudy, int offset, int pageSize) {
        Page<PostResponseDTO> posts = postRepository
                .findAllByFieldOfStudyOrderByDateTimeDesc(
                        fieldOfStudy,
                        PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
        log.info("list {}", posts);
        return posts;
    }
    /* get all posts by [fieldOfStudy, department pagination] */
    public Page<PostResponseDTO> getPostAllPostByFieldOfStudyAndDepartmentWithPagination(String fieldOfStudy,String department, int offset, int pageSize) {
        Page<PostResponseDTO> posts = postRepository
                .findAllByFieldOfStudyAndDepartmentOrderByDateTimeDesc(
                        fieldOfStudy,
                        department,
                        PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
        log.info("list {}", posts);
        return posts;
    }
    /* get all posts by [semester , fieldOfStudy, pagination] */
    public Page<PostResponseDTO> getPostAllPostBySemesterAndFieldOfStudyWithPagination(int semester, String fieldOfStudy, int offset, int pageSize) {
        Page<PostResponseDTO> posts = postRepository
                .findAllBySemesterAndFieldOfStudyOrderByDateTimeDesc(
                        semester,
                        fieldOfStudy,
                        PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
        log.info("list {}", posts);
        return posts;
    }

    /* get all posts by [semester , department, fieldOfStudy, pagination] */
    public Page<PostResponseDTO> getPostAllPostBySemesterAndFieldOfStudyAndDepartmentWithPagination(int semester, String fieldOfStudy, String department, int offset, int pageSize) {
        Page<PostResponseDTO> posts = postRepository
                .findAllBySemesterAndFieldOfStudyAndDepartmentOrderByDateTimeDesc(
                        semester,
                        fieldOfStudy,
                        department,
                        PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
        log.info("list {}", posts);
        return posts;
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
