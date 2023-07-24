package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Relative;
import lombok.Builder;
import java.util.List;

@Builder
public record StudentResponseDTO(StudentResponsePersonalInfo studentPersonalInfo,
                                 String imageUrl,
                                 Boolean isEnable,
                                 List<RelativeRegistrationDTO> relatives,
                                 List<LocationDTO> locations,
                                 IdentificationDTO identification
                                 ) {

}


