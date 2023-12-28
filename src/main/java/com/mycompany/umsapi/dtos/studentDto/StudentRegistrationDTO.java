package com.mycompany.umsapi.dtos.studentDto;

import com.mycompany.umsapi.dtos.IdentificationDTO;
import com.mycompany.umsapi.dtos.Locations;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record StudentRegistrationDTO(
        @Valid
        @NotNull(message = "اطلاعات شخصی نباید خالی باشد")
        StudentPersonalInfo studentPersonalInfo,
        @Valid
        @NotNull(message = "اطلاعات تذکره نباید خالی باشد")
        IdentificationDTO identification,
        @Valid
        @NotNull(message = "اطلاعات محل سکونت نباید خالی باشد")
        Locations locations,
        @Valid
        @NotNull(message = "اطلاعات اقارب نباید خالی باشد")
        List<RelativeRegistrationDTO> relatives,
        String role
) {
}


