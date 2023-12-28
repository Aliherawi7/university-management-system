package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.postDto.AuthorDTO;
import com.mycompany.umsapi.dtos.postDto.PostResponseDTO;
import com.mycompany.umsapi.models.GeneralPost;
import com.mycompany.umsapi.models.hrms.UserApp;
import com.mycompany.umsapi.repositories.UserRepository;
import com.mycompany.umsapi.services.FileStorageService;
import com.mycompany.umsapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PostResponseDTOMapper implements Function<GeneralPost, PostResponseDTO> {
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;
    private final FileStorageService fileStorageService;
    @Override
    public PostResponseDTO apply(GeneralPost generalPost) {
        UserApp userApp = generalPost.getAuthor();
        List<String> files = fileStorageService.getAllPostFileName(generalPost.getId());
        AuthorDTO authorDTO = AuthorDTO.builder()
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest)+ APIEndpoints.USER_PROFILE_IMAGE.getValue()+ userApp.getId()+".png")
                .build();
        return PostResponseDTO
                .builder()
                .id(generalPost.getId())
                .message(generalPost.getMessage())
                .author(authorDTO)
                .dateTime(generalPost.getDateTime())
                .images(files.stream()
                        .filter(file -> file.endsWith("png") || file.endsWith("jpeg") || file.endsWith("jpg"))
                        .map(file -> BaseURI.getBaseURI(httpServletRequest)+APIEndpoints.POST.getValue()+ generalPost.getId()+"/"+file)
                        .toList())
                .docs(files.stream()
                        .filter(file -> file.endsWith("pdf"))
                        .map(file -> BaseURI.getBaseURI(httpServletRequest)+APIEndpoints.POST.getValue()+ generalPost.getId()+"/"+file)
                        .toList())
                .isHidden(generalPost.isHidden())
                .isUpdated(generalPost.isUpdated())
                .build();
    }
}
