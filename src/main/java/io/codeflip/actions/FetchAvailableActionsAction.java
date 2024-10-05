package io.codeflip.actions;

import java.util.Arrays;
import java.util.List;

/**
 * FetchAvailableActionsAction is responsible for returning a list of available actions.
 */
public class FetchAvailableActionsAction implements Action {

    @Override
    public String execute() {
        List<String> availableActions = Arrays.asList("ExecuteCommand", "FetchAvailableActions");
        return "Available actions: " + String.join(", ", availableActions);
    }

    @Override
    public String getName() {
        return "FetchAvailableActions";
    }
}