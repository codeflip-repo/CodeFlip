package io.codeflip.actions.model;

import java.util.Map;

/**
 * Represents metadata about an action.
 * Provides information about the action's name, description, expected parameters, and return type.
 *
 * @param name The name of the action.
 * @param description A brief description of what the action does.
 * @param expectedParams A map of expected parameter names and their corresponding types.
 * @param returnType The class of the return type for the action's result.
 */
public record ActionMetadata(
        String name,
        String description,
        Map<String, Class<?>> expectedParams,
        Class<?> returnType
) {}
