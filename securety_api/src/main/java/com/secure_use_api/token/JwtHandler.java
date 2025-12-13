package com.secure_use_api.token;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;

public class JwtHandler implements SimpleTokenStringHandler {
    public static Type payloadType = new TypeToken<Map<String, Object>>() {}.getType();

    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtHandler(String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm)
            // .withIssuer("HAW-Hamburg")
            .build();
    }

    @Override
    public Optional<String> payloadFromTokenString(String tokeString) {

        try {
            DecodedJWT jwt = this.verifier.verify(tokeString);
            byte[] decodedBytes = Base64.getDecoder().decode(jwt.getPayload());
            String decodedString = new String(decodedBytes);

            return Optional.of(decodedString);
        } catch(Exception _e) {
            System.out.println(_e);
            return Optional.empty();
        }
    }

    @Override
    public String payloadToTokenString(Map<String, Object> payload) {
        return JWT.create().withPayload(payload).sign(this.algorithm);
    }

}
