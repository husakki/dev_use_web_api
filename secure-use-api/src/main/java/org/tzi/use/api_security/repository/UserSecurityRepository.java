package org.tzi.use.api_security.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.tzi.use.api_security.model.UserSecurityModel;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurityModel, UUID> {
}