package io.codeflip.actions.model;

import java.util.Map;

/**
 * Represents the input parameters for an action.
 * Provides a way to access parameter values by their keys.
 *
 * @param params A map containing the parameter keys and their corresponding values.
 */
public record ActionParams(Map<String, Object> params) {

    /**
     * Retrieves the value of a parameter by its key.
     *
     * @param key The key of the parameter to retrieve.
     * @return The value of the parameter, or null if the key is not found.
     */
    public Object get(String key) {
        return params.get(key);
    }
}
