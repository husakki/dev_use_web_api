package org.tzi.use.api_security.model;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserSecurity")
public class UserSecurityModel {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "UserId")
    private UserMetaModel userMeta;

    private String hashedSaltedPassword;

    private int failedLogInAttempts;

    private Timestamp LastLogInAt;

    public UserSecurityModel() {
    }

    public String getPassword() {
        return this.hashedSaltedPassword;
    }

    public int getFailedLogInAttempts() {
        return this.failedLogInAttempts;
    }

    public Timestamp getLastLogInAt() {
        return this.LastLogInAt;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserMeta(UserMetaModel userMeta) {
        this.userMeta = userMeta;
    }

    public void setPassword(String password) {
        this.hashedSaltedPassword = password;
    }

    public void setFailedLogInAttempts(int failedLogInAttempt) {
        this.failedLogInAttempts = failedLogInAttempt;
    }

    public void setLastLogInAt(Timestamp lastLogInAt) {
        this.LastLogInAt = lastLogInAt;
    }
}
