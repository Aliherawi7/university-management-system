package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.constants.RelationName;
import com.mycompany.umsapi.models.hrms.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    Optional<Relationship> findByRelationName(RelationName name);
}
