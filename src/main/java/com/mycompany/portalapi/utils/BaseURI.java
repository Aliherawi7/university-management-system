package com.mycompany.portalapi.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.servlet.http.HttpServletRequest;

public class BaseURI {

    public static String getBaseURI(HttpServletRequest httpServletRequest) {
        return ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build().toUriString()+"/";
    }
}
