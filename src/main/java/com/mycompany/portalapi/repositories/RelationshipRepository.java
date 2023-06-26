package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    Optional<Relationship> findByName(String name);
}
