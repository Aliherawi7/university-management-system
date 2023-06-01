package com.mycompany.portalapi.dtos;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record StudentRegistrationDTO(
     StudentPersonalInfo studentPersonalInfo,
     MultipartFile studentImage,
     IdentificationDTO identification,
     Locations locations,
     List<RelativeRegistrationDTO> relatives
) {
}


