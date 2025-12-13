package com.secure_use_api.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserAdditions")
public class UserAdditionsModel {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private UserMetaModel userMeta;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ApiTokenModel> items = new ArrayList<ApiTokenModel>();

    private int resourceTimeUsed;

    private Timestamp resourceTimeUsedLastChangedAt;

    public UserAdditionsModel() {
    }

    public int getResourceLimitUsed() {
        return this.resourceTimeUsed;
    }

    public Timestamp getResourceTimeUsedLastChangedAt() {
        return this.resourceTimeUsedLastChangedAt;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setResourceLimitUsed(int resourceTimeUsed) {
        this.resourceTimeUsed = resourceTimeUsed;
    }

    public void setUserMeta(UserMetaModel userMetaModel) {
        this.userMeta = userMetaModel;
    }
}
