package com.mycompany.portalapi.services;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.properties.FileStorageLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileStorageService {

    private final Path studentProfileImageLocation;
    private final Path userProfileImageLocation;

    public FileStorageService(FileStorageLocation fileStorageLocation) {
        this.studentProfileImageLocation = Paths
                .get(fileStorageLocation.getStudentProfileImageUploadDir())
                .toAbsolutePath()
                .normalize();
        this.userProfileImageLocation = Paths.get(fileStorageLocation.getUserProfileUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(studentProfileImageLocation);
            Files.createDirectories(userProfileImageLocation);
        } catch (IOException e) {
            System.out.println("Couldn't create the directory where the upload files will be saved. " + e.getMessage());
        }
    }

    /*
     * store the student profile image
     * */
    public void storeStudentProfileImage(MultipartFile file, long StudentId) {
        try {
            storeTheFile(file, String.valueOf(StudentId), studentProfileImageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * store the student profile image
     * */
    public void storeStudentProfileImageByteArray(byte[] file, long userId) {
        try {
            storeTheByteArray(file, String.valueOf(userId), studentProfileImageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * store the product images
     * */
    public void storeUserProfileImage(MultipartFile file, Long userId) throws IOException {
        log.info("storing for student image with id {}",userId);
        storeTheFile(file, String.valueOf(userId), userProfileImageLocation);
    }


    /*
     * store the file in the user-profile-image directory
     * */
    public void storeTheFile(MultipartFile multipartFile, String fileName, Path path) throws IOException {
        if (multipartFile == null || fileName == null) return;

        String originalFileName = StringUtils.getFilename(multipartFile.getOriginalFilename());

        // Check if the file's name contains valid  characters or not
        if (fileName.contains("..")) {
            throw new RuntimeException("Sorry! File name which contains invalid path sequence " + originalFileName);
        }
        assert originalFileName != null;
        //String extension = originalFileName.split("\\.")[1];
        path = path.resolve(fileName + "." + "png");
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public void storeTheByteArray(byte[] multipartFile, String fileName, Path path) throws IOException {
        if (multipartFile == null || fileName == null) return;
        File file = new File(path.resolve(fileName + ".png").toUri());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile);
        fileOutputStream.close();
    }


    /*
     * get the image by file name and file location
     * */
    public byte[] getFile(String fileName, Path path) {
        log.info("looking for student image with id {}",fileName);
        File image = Stream
                .of(Objects.requireNonNull(new File(path.toUri()).listFiles()))
                .filter(item -> item.getName().split("\\.")[0].equalsIgnoreCase(fileName))
                .findFirst()
                .orElse(null);
        if (image == null) {
            throw new ResourceNotFoundException("File not found with provided student id");
        }
        byte[] imageBytes = new byte[(int) image.length()];

        try (FileInputStream inputStream = new FileInputStream(image)) {
            inputStream.read(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    /*
     * get the user image
     * */

    public byte[] getUserImage(String userId) {
        log.info("looking for user image with id {}",userId);
        return getFile(userId, userProfileImageLocation);
    }

    /*
     * get the student profile image
     * */
    public byte[] getStudentImage(String studentId) {
        log.info("looking for student image with id {}",studentId);
        return getFile(studentId, studentProfileImageLocation);
    }

}
