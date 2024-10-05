package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class for managing and retrieving Action instances.
 * This class is responsible for registering actions and providing access to them.
 */
public class ActionFactory {
    private final Map<String, Action<?>> actions = new HashMap<>();

    /**
     * Constructs a new ActionFactory and registers the default actions.
     */
    public ActionFactory() {
        registerAction(new ExecuteCommandAction());
        registerAction(new WriteToFileAction());
        registerAction(new FetchAvailableActions(this));
        registerAction(new ReadFileAction());
    }

    /**
     * Registers an action with the factory.
     *
     * @param action The action to register.
     */
    public void registerAction(Action<?> action) {
        actions.put(action.getMetadata().name(), action);
    }

    /**
     * Retrieves an action by its name.
     *
     * @param name The name of the action to retrieve.
     * @return The requested Action instance.
     * @throws ActionNotFoundException if no action with the given name is found.
     */
    public Action<?> getAction(String name) throws ActionNotFoundException {
        Action<?> action = actions.get(name);
        if (action == null) {
            throw new ActionNotFoundException("Action not found: " + name);
        }
        return action;
    }

    /**
     * Retrieves metadata for all available actions.
     *
     * @return A map of action names to their corresponding metadata.
     */
    public Map<String, ActionMetadata> getAvailableActions() {
        Map<String, ActionMetadata> availableActions = new HashMap<>();
        for (Action<?> action : actions.values()) {
            availableActions.put(action.getMetadata().name(), action.getMetadata());
        }
        return availableActions;
    }

    /**
     * Exception thrown when an attempt is made to retrieve a non-existent action.
     */
    public static class ActionNotFoundException extends Exception {
        /**
         * Constructs a new ActionNotFoundException with the specified detail message.
         *
         * @param message The detail message (which is saved for later retrieval by the getMessage() method)
         */
        public ActionNotFoundException(String message) {
            super(message);
        }
    }
}