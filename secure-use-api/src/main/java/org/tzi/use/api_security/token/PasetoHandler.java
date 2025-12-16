package org.tzi.use.api_security.token;

import java.util.Map;
import java.util.Optional;

public class PasetoHandler implements SimpleTokenStringHandler {
    @Override
    public Optional<String> payloadFromTokenString(String tokeString) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'payloadFromTokenString'");
    }

    @Override
    public String payloadToTokenString(Map<String,Object> payload) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'payloadToTokenString'");
    }
    
}
