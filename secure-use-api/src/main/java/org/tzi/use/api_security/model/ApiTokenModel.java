package org.tzi.use.api_security.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ApiToken")
public class ApiTokenModel {

    @Id
    private UUID uid;

    @Lob // Allow larger text storage
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private UUID owner;

    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "owner", insertable = false, updatable = false)
    private UserAdditionsModel userAdditions;

    public ApiTokenModel() {
    }

    public UUID getUid() {
        return this.uid;
    }

    public String getToken() {
        return this.token;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOwner(UUID ownerId) {
        this.owner = ownerId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
