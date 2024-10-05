package io.codeflip.actions.model;

/**
 * Represents an executable action in the system.
 * Actions are the core operational units, capable of performing specific tasks.
 *
 * @param <T> The type of result this action produces when executed.
 */
public interface Action<T> {

    /**
     * Executes the action with the given parameters.
     *
     * @param params The input parameters for the action.
     * @return An ActionResult containing the result of the action execution and its status.
     * @throws ActionExecutionException if an error occurs during execution.
     */
    ActionResult<T> execute(ActionParams params) throws ActionExecutionException;

    /**
     * Retrieves metadata about the action, including its name, description,
     * expected parameters, and return value type.
     *
     * @return An ActionMetadata object containing the action's metadata.
     */
    ActionMetadata getMetadata();
}