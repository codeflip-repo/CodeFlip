package io.codeflip.actions.model;

/**
 * Represents the result of an action execution.
 * Encapsulates both the result data and the execution status.
 *
 * @param <T> The type of the result data.
 * @param result The data produced by the action execution.
 * @param status The status of the action execution (e.g., SUCCESS or FAILURE).
 */
public record ActionResult<T>(T result, ActionStatus status) {}