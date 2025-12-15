package org.tzi.use.api_security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.tzi.use.api_security.model.UserMetaModel;

@Repository
public interface UserMetaRepository extends JpaRepository<UserMetaModel, UUID> {
    Optional<UserMetaModel> findByEmail(String email);
}