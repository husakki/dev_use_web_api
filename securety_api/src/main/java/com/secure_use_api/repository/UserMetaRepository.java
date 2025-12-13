package com.secure_use_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.secure_use_api.model.UserMetaModel;

@Repository
public interface UserMetaRepository extends JpaRepository<UserMetaModel, UUID> {
    Optional<UserMetaModel> findByEmail(String email);
}