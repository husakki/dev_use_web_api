package com.secure_use_api.token;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

public abstract class AbstractToken {
    protected static Type payloadType = new TypeToken<Map<String, Object>>() {}.getType();

    protected static SimpleTokenStringHandler tokenStringHandler;
}
