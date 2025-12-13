package com.secure_use_api.model;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RefreshToken")
public class RefreshTokenModel {

    @Id
    private UUID uid;

    @Column(nullable = false, unique = true)
    private UUID spouseId;

    @Column(nullable = false)
    private Timestamp expireAt;

    public RefreshTokenModel() {
    }

    public UUID getUid() {
        return this.uid;
    }

    public UUID getSpouseId() {
        return this.spouseId;
    }

    public Timestamp getExpireAt() {
        return this.expireAt;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public void setSpouse(UUID spouseId) {
        this.spouseId = spouseId;
    }

    public void setExpireAt(Timestamp expireAt) {
        this.expireAt = expireAt;
    }
}
