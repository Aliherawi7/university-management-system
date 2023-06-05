package com.mycompany.portalapi.constants;

public enum APIEndpoints {
    USER_PROFILE_IMAGE("api/v1/files/user-profiles/"),
    POST("api/v1/files/post-files/"),
    STUDENT_PROFILE_IMAGE("api/v1/files/student-profiles/");


    final String value;

    APIEndpoints(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
