package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.LocationDTO;
import com.mycompany.umsapi.models.hrms.Location;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LocationDTOMapper implements Function<Location, LocationDTO> {
    @Override
    public LocationDTO apply(Location location) {
        return LocationDTO
                .builder()
                .city(location.getCity())
                .district(location.getDistrict())
                .villageOrQuarter(location.getVillageOrQuarter())
                .current(location.isCurrent())
                .build();
    }
}
