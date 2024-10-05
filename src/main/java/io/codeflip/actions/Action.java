package io.codeflip.actions;

/**
 * The Action interface represents an operation that can be performed.
 * All concrete actions in the system should implement this interface.
 */
public interface Action {

    /**
     * Executes the action.
     *
     * @return A String representing the result of the action execution.
     * @throws Exception if an error occurs during execution.
     */
    String execute() throws Exception;

    /**
     * Returns the name of the action.
     *
     * @return A String representing the name of the action.
     */
    String getName();
}