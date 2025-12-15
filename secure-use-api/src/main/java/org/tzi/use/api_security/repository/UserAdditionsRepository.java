package org.tzi.use.api_security.repository;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.tzi.use.api_security.model.UserAdditionsModel;

@Repository
public interface UserAdditionsRepository extends JpaRepository<UserAdditionsModel, UUID> {
    @Transactional
    @Query(value = "SELECT * FROM USER_ADDITIONS u WHERE u.user_id=?1 AND u.resource_time_used_last_changed_at BETWEEN ?2 AND ?3", nativeQuery = true)
    UserAdditionsModel findByUidAndToday(UUID uid, Timestamp todayStart, Timestamp todayEnd);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ADDITIONS u SET u.resource_time_used=?2, u.resource_time_used_last_changed_at=?3 WHERE u.user_id=?1", nativeQuery = true)
    int UpdateResourceTimeUsedAndLastChangedAtByUid(UUID uid, long resourceTimeUsed,
            Timestamp resourceTimeUsedLTimestamp);
}