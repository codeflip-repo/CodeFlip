package io.codeflip.actions;

import io.codeflip.actions.ActionFactory.ActionNotFoundException;
import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;

import java.util.Map;

/**
 * Main class demonstrating the usage of the CodeFlip Action System.
 * This class provides examples of how to use different actions through the ActionFactory.
 */
public class Main {

    /**
     * The entry point of the application.
     * Demonstrates the usage of various actions in the system.
     *
     * @param args Command line arguments (not used in this demonstration)
     */
    public static void main(String[] args) {
        ActionFactory factory = new ActionFactory();

        try {
            demonstrateExecuteCommand(factory);
            demonstrateWriteToFile(factory);
            demonstrateReadFile(factory);
            demonstrateFetchAvailableActions(factory);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Demonstrates the usage of the ExecuteCommand action.
     * Executes a simple echo command and prints the output.
     *
     * @param factory The ActionFactory used to create the action
     * @throws ActionNotFoundException if the ExecuteCommand action is not found
     * @throws ActionExecutionException if an error occurs during action execution
     */
    private static void demonstrateExecuteCommand(ActionFactory factory) throws ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating ExecuteCommand action:");
        Action<?> action = factory.getAction("ExecuteCommand");
        ActionParams params = new ActionParams(Map.of("command", "echo Hello, Action System!"));
        ActionResult<?> result = action.execute(params);

        if (result.status() == ActionStatus.SUCCESS) {
            System.out.println("Command output: " + result.result());
        } else {
            System.out.println("Command execution failed");
        }
        System.out.println();
    }

    /**
     * Demonstrates the usage of the WriteToFile action.
     * Writes a test string to a file in the system's temporary directory.
     *
     * @param factory The ActionFactory used to create the action
     * @throws ActionNotFoundException if the WriteToFile action is not found
     * @throws ActionExecutionException if an error occurs during action execution
     */
    private static void demonstrateWriteToFile(ActionFactory factory) throws ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating WriteToFile action:");
        Action<?> action = factory.getAction("WriteToFile");
        String content = "Hello, this is a test file content.";
        String filePath = System.getProperty("java.io.tmpdir") + "/test_file.txt";
        ActionParams params = new ActionParams(Map.of(
                "content", content,
                "filePath", filePath
        ));
        ActionResult<?> result = action.execute(params);

        if (result.status() == ActionStatus.SUCCESS) {
            System.out.println("File written successfully to: " + filePath);
        } else {
            System.out.println("Failed to write file");
        }
        System.out.println();
    }

    /**
     * Demonstrates the usage of the ReadFile action.
     * Reads the content of the file created by the WriteToFile action demonstration.
     *
     * @param factory The ActionFactory used to create the action
     * @throws ActionNotFoundException if the ReadFile action is not found
     * @throws ActionExecutionException if an error occurs during action execution
     */
    private static void demonstrateReadFile(ActionFactory factory) throws ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating ReadFile action:");
        Action<?> action = factory.getAction("ReadFile");
        String filePath = System.getProperty("java.io.tmpdir") + "/test_file.txt";
        ActionParams params = new ActionParams(Map.of("filePath", filePath));
        ActionResult<?> result = action.execute(params);

        if (result.status() == ActionStatus.SUCCESS) {
            System.out.println("File content: " + result.result());
        } else {
            System.out.println("Failed to read file");
        }
        System.out.println();
    }

    /**
     * Demonstrates the usage of the FetchAvailableActions action.
     * Retrieves and displays information about all available actions in the system.
     *
     * @param factory The ActionFactory used to create the action
     * @throws ActionFactory.ActionNotFoundException if the FetchAvailableActions action is not found
     * @throws ActionExecutionException if an error occurs during action execution
     */
    private static void demonstrateFetchAvailableActions(ActionFactory factory) throws ActionFactory.ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating FetchAvailableActions action:");
        Action<?> action = factory.getAction("FetchAvailableActions");
        ActionResult<?> result = action.execute(new ActionParams(Map.of()));

        if (result.status() == ActionStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            Map<String, ActionMetadata> availableActions = (Map<String, ActionMetadata>) result.result();
            System.out.println("Available Actions:");
            for (Map.Entry<String, ActionMetadata> entry : availableActions.entrySet()) {
                ActionMetadata metadata = entry.getValue();
                System.out.println("- " + metadata.name() + ": " + metadata.description());
            }
        } else {
            System.out.println("Failed to fetch available actions");
        }
        System.out.println();
    }
}