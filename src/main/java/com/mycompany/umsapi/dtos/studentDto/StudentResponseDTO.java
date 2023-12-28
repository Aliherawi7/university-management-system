package com.mycompany.umsapi.dtos.studentDto;

import com.mycompany.umsapi.dtos.IdentificationDTO;
import com.mycompany.umsapi.dtos.LocationDTO;
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


