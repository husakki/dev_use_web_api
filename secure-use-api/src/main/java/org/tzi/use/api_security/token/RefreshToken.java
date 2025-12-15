package org.tzi.use.api_security.token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.Gson;
import org.tzi.use.api_security.util.Config;

public class RefreshToken extends AbstractToken {
    static long expirationTime = 2592000000L; // 30 day
    protected static SimpleTokenStringHandler tokenStringHandler = new JwtHandler(
            Config.getInstance().getSecretRefreshTokenKey());

    protected final UUID uid;
    protected final UUID spouseId;
    protected final Date expiresAt;

    public class InnerAccessToken {
        public String jti;
        public String spouseId;
        public Long exp;
    }

    public RefreshToken(UUID spouseId) {
        this.uid = UUID.randomUUID();
        this.spouseId = spouseId;
        this.expiresAt = new Date(System.currentTimeMillis() + expirationTime);
    }

    protected RefreshToken(UUID uid, UUID spouseId, Date expiresAt) {
        this.uid = uid;
        this.spouseId = spouseId;
        this.expiresAt = expiresAt;
    }

    static public Optional<RefreshToken> fromTokenString(String tokeString) throws IllegalArgumentException {
        Optional<String> payloadString = tokenStringHandler.payloadFromTokenString(tokeString);

        if (payloadString.isPresent()) {
            Gson json = new Gson();
            InnerAccessToken payload = json.fromJson(payloadString.get(), InnerAccessToken.class);

            String uidString = payload.jti;
            String spouseIdString = payload.spouseId;
            Long expiresAtLong = payload.exp;

            if (uidString == null || uidString.isEmpty()) {
                throw new IllegalArgumentException("Missing uid");
            }

            if (spouseIdString == null || spouseIdString.isEmpty()) {
                throw new IllegalArgumentException("Missing userId");
            }

            if (expiresAtLong == null) {
                throw new IllegalArgumentException("expires date is null");
            }

            UUID uid = UUID.fromString(uidString);
            UUID spouseId = UUID.fromString(spouseIdString);
            Date expiresAtDate = new Date(expiresAtLong * 1000);

            return Optional.of(new RefreshToken(uid, spouseId, expiresAtDate));
        }

        return Optional.empty();
    }

    public String toTokenString() {
        Map<String, Object> payload = new HashMap<>();

        payload.put("jti", this.uid.toString());
        payload.put("spouseId", this.spouseId.toString());
        payload.put("iat", new Date().getTime() / 1000);
        payload.put("exp", this.expiresAt.getTime() / 1000);

        return tokenStringHandler.payloadToTokenString(payload);
    }

    public UUID getUid() {
        return this.uid;
    }

    public UUID getSpouseId() {
        return this.spouseId;
    }

    public Date getExpiresAt() {
        return this.expiresAt;
    }

}
