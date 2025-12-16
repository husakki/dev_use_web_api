package org.tzi.use.api_security.token;

import java.util.Map;
import java.util.Optional;

public interface SimpleTokenStringHandler {
    Optional<String> payloadFromTokenString(String tokeString);
    String payloadToTokenString(Map<String,Object> payload);
}
