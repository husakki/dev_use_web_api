package com.secure_use_api.repository;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import com.secure_use_api.model.RefreshTokenModel;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM REFRESH_TOKEN r WHERE r.uid=?1 AND r.spouse_id=?2 AND r.expire_at>?3", nativeQuery = true)
    int deleteExpiredToken(UUID uid, UUID spouseId, Date currentTime);
}