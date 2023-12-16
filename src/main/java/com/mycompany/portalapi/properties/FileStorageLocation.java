package com.mycompany.portalapi.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileStorageLocation {

    private String studentProfileImageUploadDir;
    private String postFiles;


    public FileStorageLocation(
            @Value("${file.userApp.profile.upload.dir}") String studentProfileImageUploadDir,
            @Value("${file.generalPost.upload.dir}") String postFiles) {
        this.studentProfileImageUploadDir = System.getProperty("user.dir") + studentProfileImageUploadDir;
        this.postFiles = System.getProperty("user.dir") + postFiles;
    }

    public String getStudentProfileImageUploadDir() {
        return studentProfileImageUploadDir;
    }

    public void setStudentProfileUploadDir(String studentProfileImageUploadDir) {
        this.studentProfileImageUploadDir = studentProfileImageUploadDir;
    }

    public String getPostFiles() {
        return postFiles;
    }

    public void setPostFiles(String postFiles) {
        this.postFiles = postFiles;
    }
}
