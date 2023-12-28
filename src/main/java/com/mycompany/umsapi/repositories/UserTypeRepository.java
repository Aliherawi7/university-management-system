package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.constants.UserTypeName;
import com.mycompany.umsapi.models.hrms.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    UserType findByUserTypeName(UserTypeName userTypeName);
}
