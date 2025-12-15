package org.tzi.use.api_security.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.tzi.use.api_security.model.ApiTokenModel;

@Repository
public interface ApiTokenRepository extends JpaRepository<ApiTokenModel, UUID> {
    List<ApiTokenModel> findByOwner(UUID ownerId);

    int deleteByUidAndOwner(UUID uid, UUID owner);

    ApiTokenModel findByUidAndOwner(UUID uid, UUID ownerId);
}