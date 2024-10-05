package io.codeflip.actions;

import java.util.List;

/**
 * ActionFactory is responsible for creating instances of different Action types.
 */
public class ActionFactory {

    /**
     * Creates and returns an Action based on the given name and parameters.
     *
     * @param actionName The name of the action to create.
     * @param params Additional parameters required for the action.
     * @return An instance of the requested Action.
     * @throws IllegalArgumentException if the action name is not recognized.
     */
    public static Action createAction(String actionName, Object... params) {
        switch (actionName) {
            case "ExecuteCommand":
                if (params.length == 1 && params[0] instanceof List) {
                    return new ExecuteCommandAction((List<String>) params[0]);
                }
                throw new IllegalArgumentException("ExecuteCommand action requires a List<String> parameter");
            case "FetchAvailableActions":
                return new FetchAvailableActionsAction();
            default:
                throw new IllegalArgumentException("Unknown action: " + actionName);
        }
    }
}