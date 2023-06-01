package com.mycompany.portalapi.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class BaseURI {

    public static String getBaseURI(HttpServletRequest httpServletRequest) {
        return ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build().toUriString()+"/";
    }
}
