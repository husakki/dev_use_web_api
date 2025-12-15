package org.tzi.use.api_security.model;

public class UserComposite {
    private UserMetaModel userMeta;
    private UserSecurityModel userSecurity;

    public UserComposite(UserMetaModel userMeta, UserSecurityModel userSecurity) {
        this.userMeta = userMeta;
        this.userSecurity = userSecurity;
    }

    // Getters and Setters
    public UserMetaModel getUserMeta() {
        return userMeta;
    }

    public void setUserMeta(UserMetaModel userMeta) {
        this.userMeta = userMeta;
    }

    public UserSecurityModel getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurityModel userSecurity) {
        this.userSecurity = userSecurity;
    }
}