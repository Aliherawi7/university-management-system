package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.constants.RelationName;
import com.mycompany.portalapi.models.hrms.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    Optional<Relationship> findByRelationName(RelationName name);
}
