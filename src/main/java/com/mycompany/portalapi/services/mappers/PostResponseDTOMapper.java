package com.mycompany.portalapi.services.mappers;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.AuthorDTO;
import com.mycompany.portalapi.dtos.PostResponseDTO;
import com.mycompany.portalapi.models.Post;
import com.mycompany.portalapi.models.UserApp;
import com.mycompany.portalapi.repositories.UserRepository;
import com.mycompany.portalapi.services.FileStorageService;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PostResponseDTOMapper implements Function<Post, PostResponseDTO> {
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;
    private final FileStorageService fileStorageService;
    @Override
    public PostResponseDTO apply(Post post) {
        UserApp userApp = userRepository.findById(post.getAuthorId()).get();
        List<String> files = fileStorageService.getAllPostFileName(post.getId());
        AuthorDTO authorDTO = AuthorDTO.builder()
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest)+ APIEndpoints.USER_PROFILE_IMAGE.getValue()+ userApp.getId()+".png")
                .build();
        return PostResponseDTO
                .builder()
                .id(post.getId())
                .message(post.getMessage())
                .author(authorDTO)
                .department(post.getDepartment())
                .fieldOfStudy(post.getFieldOfStudy())
                .dateTime(post.getDateTime())
                .images(files.stream()
                        .filter(file -> file.endsWith("png") || file.endsWith("jpeg") || file.endsWith("jpg"))
                        .map(file -> BaseURI.getBaseURI(httpServletRequest)+APIEndpoints.POST.getValue()+post.getId()+"/"+file)
                        .toList())
                .docs(files.stream()
                        .filter(file -> file.endsWith("pdf"))
                        .map(file -> BaseURI.getBaseURI(httpServletRequest)+APIEndpoints.POST.getValue()+post.getId()+"/"+file)
                        .toList())
                .isPublic(post.isPublic())
                .semester(post.getSemester())
                .isHidden(post.isHidden())
                .isUpdated(post.isUpdated())
                .build();
    }
}
