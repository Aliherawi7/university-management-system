package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.postDto.PostRequestDTO;
import com.mycompany.portalapi.dtos.postDto.PostResponseDTO;
import com.mycompany.portalapi.dtos.postDto.PostSuccessfulRegistrationDTO;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.GeneralPost;
import com.mycompany.portalapi.models.hrms.UserApp;
import com.mycompany.portalapi.repositories.PostRepository;
import com.mycompany.portalapi.services.mappers.PostResponseDTOMapper;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralPostService {
    private final PostRepository postRepository;
    private final HttpServletRequest httpServletRequest;
    private final FileStorageService fileStorageService;
    private final PostResponseDTOMapper postResponseDTOMapper;
    private final UserService userService;

    public PostSuccessfulRegistrationDTO addPost(PostRequestDTO postRequestDTO) {
        GeneralPost generalPost = addRawPost(postRequestDTO);
        return PostSuccessfulRegistrationDTO.builder()
                .message("پست موفقانه ذخیره شد")
                .postId(generalPost.getId())
                .statusCode(HttpStatus.CREATED.value())
                .filesUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST.getValue() + generalPost.getId())
                .build();
    }

    public GeneralPost addRawPost(PostRequestDTO postRequestDTO) {
        UserApp userApp = userService.getUserAppByeEmail(postRequestDTO.authorEmail().toLowerCase());
        GeneralPost savedGeneralPost = GeneralPost.builder()
                .author(userApp)
                .dateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .message(postRequestDTO.message())
                .build();
        return postRepository.save(savedGeneralPost);
    }

    public GeneralPost getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with provided id: " + postId));
    }

    public PostResponseDTO getPost(Long postId) {
        GeneralPost generalPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with provided id: " + postId));
        return postResponseDTOMapper.apply(generalPost);
    }

    /* this is method is for student client which is going to
    return post by this student information*/
    public Page<PostResponseDTO> getPostAllPostByPagination(int offset, int pageSize) {
        return postRepository
                .fetchAllPosts(PageRequest.of(offset, pageSize))
                .map(postResponseDTOMapper);
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
    public PostSuccessfulRegistrationDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        GeneralPost generalPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("پست مورد نظر یافت نشد!"));
        generalPost.setMessage(postRequestDTO.message());
        generalPost.setDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
        generalPost.setUpdated(true);
        postRepository.save(generalPost);
        return PostSuccessfulRegistrationDTO.builder()
                .message("پست موفقانه ذخیره شد")
                .postId(generalPost.getId())
                .statusCode(HttpStatus.CREATED.value())
                .filesUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.POST.getValue() + generalPost.getId())
                .build();

    }

    public void toggleHideShowPost(Long id) {
        GeneralPost generalPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("پست مورد نظر یافت نشد!"));
        generalPost.setHidden(!generalPost.isHidden());
        postRepository.save(generalPost);
    }

    public void deletePost(Long id) {

        postRepository.deleteById(id);

        //TODO: remove related files as well
    }


}
