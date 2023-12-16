package com.mycompany.portalapi.dtos.studentDto;

import com.mycompany.portalapi.dtos.IdentificationDTO;
import com.mycompany.portalapi.dtos.LocationDTO;
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


