package com.mycompany.umsapi.services;


import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.constants.RoleName;
import com.mycompany.umsapi.constants.UserTypeName;
import com.mycompany.umsapi.dtos.LoginResponse;
import com.mycompany.umsapi.dtos.UpdateUserDTO;
import com.mycompany.umsapi.dtos.users.UserRolesDTO;
import com.mycompany.umsapi.exceptions.AccountLockException;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.exceptions.UserLockException;
import com.mycompany.umsapi.models.hrms.Role;
import com.mycompany.umsapi.models.hrms.Student;
import com.mycompany.umsapi.models.hrms.UserApp;
import com.mycompany.umsapi.models.hrms.UserType;
import com.mycompany.umsapi.repositories.RoleRepository;
import com.mycompany.umsapi.repositories.StudentRepository;
import com.mycompany.umsapi.repositories.UserRepository;
import com.mycompany.umsapi.repositories.UserTypeRepository;
import com.mycompany.umsapi.security.JwtUtils;
import com.mycompany.umsapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpServletRequest;
    private final StudentRepository studentRepository;
    private final UserTypeRepository userTypeRepository;

    public void registerUser(UserApp userApp) {
        validateUser(userApp);
        userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
        userRepository.save(userApp);
    }

    public void registerStudentAsUser(Student student){
        Role role = roleRepository
                .findByRoleName(RoleName.STUDENT)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role name"));
        UserType userType = userTypeRepository.findByUserTypeName(UserTypeName.STUDENT);
        registerUser(UserApp
                .builder()
                .id(student.getId())
                .email(student.getEmail().toLowerCase())
                .roles(Set.of(role))
                .password(student.getPassword())
                .isEnabled(true)
                .lastname(student.getLastName())
                .userType(userType)
                .name(student.getName())
                .build());
    }

    public void registerTeacherAsUser(){

    }
    public void registerStaffAsUser(){

    }


    public LoginResponse updateUser(UpdateUserDTO updateUserDTO, String email) {
        UserApp userApp = getUserAppByeId(updateUserDTO.userId());
        String previousEmail = userApp.getEmail();
        if (!userApp.isEnabled()) {
            throw new AccountLockException("Your account is locked! please wait until admin unlock it");
        }
        /* check if email has already taken by another user */
        if (isExistByEmail(updateUserDTO.email()) && !previousEmail.equalsIgnoreCase(updateUserDTO.email())) {
            throw new IllegalArgumentException("email is already taken");
        }
        if (!previousEmail.equalsIgnoreCase(updateUserDTO.email())) {
            userApp.setEmail(updateUserDTO.email());
        }
        if(!isAdmin(email)){
            if(!passwordEncoder.matches(updateUserDTO.previousPassword(), userApp.getPassword())){
                throw new IllegalArgumentException("Password is wrong");
            }
        }

        userApp.setPassword(passwordEncoder.encode(updateUserDTO.newPassword()));
        userRepository.save(userApp);
        if(userApp.getRoles().stream().noneMatch(item -> item.getRoleName().getValue().equals(RoleName.ADMIN.getValue()))){
            Student studentByEmail = studentRepository.findByEmail(previousEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with provided email"));
            studentByEmail.setEmail(updateUserDTO.email());
            studentRepository.save(studentByEmail);
        }
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId() + ".png")
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList()).email(userApp.getEmail()).token(jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30))
                .build();
    }



    public void validateUser(UserApp userApp) {
        if (userApp.getId() == null ||
                userApp.getName() == null ||
                userApp.getEmail() == null ||
                userApp.getLastname() == null ||
                userApp.getRoles() == null ||
                userApp.getPassword() == null)
            throw new IllegalArgumentException("Invalid user information");
    }

    public void lockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with provided id: "+ userId));
        userApp.setIsEnabled(false);
        userRepository.save(userApp);
    }

    public void unLockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with provided id: "+ userId));
        userApp.setIsEnabled(true);
        userRepository.save(userApp);
    }

    public boolean checkIfUserIfLockByEmail(String email) {
        if(isEnable(email)){
            return true;
        }
        throw new UserLockException("your account has been locked by admin!");
    }

    public UserApp getUserAppByeId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid User id"));
    }

    public UserApp getUserAppByeEmail(String email){
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid User email"));
    }
    public boolean isExistByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean isEnable(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with provided email: "+ email))
                .isEnabled();
    }


    /* update user role */
    public void updateUserRole(Long userId, Integer roleId){
        UserApp userApp = getUserAppByeId(userId);
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Invalid role id: "+ roleId));
        userApp.getRoles().add(role);
        userRepository.save(userApp);
    }
    public boolean isAdmin(String email){
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user credential! please try again"))
                .getRoles().stream().anyMatch(role -> role.getRoleName().getValue().equals(RoleName.ADMIN.getValue()));
    }


    public boolean isAdmin(UserApp userApp){
        return userApp.getRoles().stream().anyMatch(role -> role.getRoleName().getValue().equals(RoleName.ADMIN.getValue()));
    }


    public void deleteUser(String email){
        userRepository.deleteUserAppByEmail(email);
    }


    public UserRolesDTO getUserRolesById(Long userId) {
        UserApp userApp = getUserAppByeId(userId);
        Set<Role> roles = userApp.getRoles();
        return UserRolesDTO.builder()
                .userType(userApp.getUserType())
                .userId(userId)
                .roles(roles)
                .email(userApp.getEmail())
                .build();
    }
}
