package io.codeflip.actions.model;

/**
 * Exception thrown when an error occurs during the execution of an action.
 */
public class ActionExecutionException extends Exception {

    /**
     * Constructs a new ActionExecutionException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ActionExecutionException(String message) {
        super(message);
    }

    /**
     * Constructs a new ActionExecutionException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause The cause (which is saved for later retrieval by the getCause() method)
     */
    public ActionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
