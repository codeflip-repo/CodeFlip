package io.codeflip.actions.model;

import java.util.Map;

public record ActionRequest(Map<String, Object> params) {
    public ActionRequest {
        params = params != null ? Map.copyOf(params) : Map.of();
    }
}