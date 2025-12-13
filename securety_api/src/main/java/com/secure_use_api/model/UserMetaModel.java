package com.secure_use_api.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserMeta")
public class UserMetaModel {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    private String provider;

    private String externalId;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp lastChangedAt;

    @OneToOne(mappedBy = "userMeta", cascade = CascadeType.ALL)
    private UserSecurityModel userSecurityModel;

    @OneToOne(mappedBy = "userMeta", cascade = CascadeType.ALL)
    private UserAdditionsModel userAdditionsModel;

    public UserMetaModel() {
    }

    public UUID getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}
