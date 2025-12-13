package com.secure_use_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.secure_use_api.model.UserSecurityModel;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurityModel, UUID> {
}