package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.models.Relative;
import lombok.Builder;
import java.util.List;

@Builder
public record StudentResponseDTO(StudentResponsePersonalInfo studentPersonalInfo,
                                 String imageUrl,
                                 List<Relative> relatives,
                                 Locations locations,
                                 IdentificationDTO identification
                                 ) {

}


