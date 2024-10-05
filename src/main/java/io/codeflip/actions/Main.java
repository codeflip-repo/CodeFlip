package io.codeflip.actions;

import java.util.Arrays;

/**
 * Main class to demonstrate the usage of actions.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Fetch available actions
            Action fetchAction = ActionFactory.createAction("FetchAvailableActions");
            System.out.println(fetchAction.execute());

            // Execute a command
            Action executeAction = ActionFactory.createAction("ExecuteCommand", Arrays.asList("echo Hello, World!"));
            System.out.println(executeAction.execute());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
