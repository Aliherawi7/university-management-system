package com.mycompany.portalapi.services;
import com.mycompany.portalapi.exceptions.InvalidFileException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.properties.FileStorageLocation;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileStorageService {
    private final Path studentProfileImageLocation;
    private final Path post;
    private final FileStorageLocation fileStorageLocation;
    public FileStorageService(FileStorageLocation fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
        this.studentProfileImageLocation = Paths
                .get(fileStorageLocation.getStudentProfileImageUploadDir())
                .toAbsolutePath()
                .normalize();
        this.post = Paths.get(fileStorageLocation.getPostFiles())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(studentProfileImageLocation);
            Files.createDirectories(post);
        } catch (IOException e) {
            System.out.println("Couldn't create the directory where the upload files will be saved. " + e.getMessage());
        }
    }

    public Path createDirectoryForPost(String path, Long postId){
        Path dir = Paths.get(path +"/"+postId)
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(dir);
            return dir;
        } catch (IOException e) {
            System.out.println("Couldn't create the directory where the upload files will be saved. " + e.getMessage());
        }
        return null;
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
        storeTheFile(file, String.valueOf(userId), post);
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
        log.info("looking for file  with name {}",fileName);
        File image = Stream
                .of(Objects.requireNonNull(new File(path.toUri()).listFiles()))
                .filter(item -> item.getName().split("\\.")[0].equalsIgnoreCase(fileName.split("\\.")[0]))
                .findFirst()
                .orElse(null);
        if (image == null) {
            throw new ResourceNotFoundException("File not found with provided name");
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
        return getFile(userId, post);
    }

    /*
     * get the student profile image
     * */
    public byte[] getStudentImage(String studentId) {
        log.info("looking for student image with id {}",studentId);
        return getFile(studentId, studentProfileImageLocation);
    }


    public void storePostFiles(List<MultipartFile> files, Long postId) {
        final Path newPath = createDirectoryForPost(fileStorageLocation.getPostFiles(), postId);
        files.forEach(file -> {
            if(file == null ||file.isEmpty()){
                throw new InvalidFileException("invalid file");
            }
            String originalFileName = StringUtils.getFilename(file.getOriginalFilename());
            if (originalFileName == null || originalFileName.contains("..")) {
                throw new InvalidFileException("Sorry! File name which contains invalid path sequence " + originalFileName);
            }
            String extension = originalFileName.split("\\.")[1];
            log.info("origin name:{}",originalFileName);
            log.info("extension:{}",extension);
            Path storingDir = newPath.resolve(originalFileName.split("\\.")[0] + "." + extension);
            try {
                Files.copy(file.getInputStream(), storingDir, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<String> getAllPostFileName(Long postId){
        Path postDir = Path.of(fileStorageLocation.getPostFiles()+"/"+postId);
        File directory = new File(postDir.toUri());
        File[] files = directory.listFiles();
//        for test
        if(files == null){
            files = new File[0];
        }
        return Stream
                .of(files)
                .map(file -> {
                    return StringUtils.getFilename(file.getName());
                }).toList();
    }
    public byte[] getPostFile(String fileName, Long postId){
        Path path = Path.of(fileStorageLocation.getPostFiles()+"/"+postId);
        return getFile(fileName, path);
    }
}
