package com.mycompany.portalapi.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileStorageLocation {

    private String studentProfileImageUploadDir;
    private String userProfileImageUploadDir;


    public FileStorageLocation(
            @Value("${file.user.profile.upload.dir}") String studentProfileImageUploadDir,
            @Value("${file.product.upload.dir}") String userProfileImageUploadDir) {
        this.studentProfileImageUploadDir = studentProfileImageUploadDir;
        this.userProfileImageUploadDir = userProfileImageUploadDir;
    }

    public String getStudentProfileImageUploadDir() {
        return studentProfileImageUploadDir;
    }

    public void setStudentProfileUploadDir(String studentProfileImageUploadDir) {
        this.studentProfileImageUploadDir = studentProfileImageUploadDir;
    }

    public String getUserProfileUploadDir() {
        return userProfileImageUploadDir;
    }

    public void setUserProfileImageUploadDir(String userProfileImageUploadDir) {
        this.userProfileImageUploadDir = userProfileImageUploadDir;
    }

}
