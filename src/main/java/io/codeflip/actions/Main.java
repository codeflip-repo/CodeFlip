package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import io.codeflip.actions.model.CommandResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Main class to demonstrate the usage of the Action system.
 * This class shows how to create an ActionFactory, execute different actions,
 * and handle their results.
 */
public class Main {

    /**
     * The entry point of the application.
     *
     * @param args Command line arguments (not used in this demonstration).
     */
    public static void main(String[] args) {
        ActionFactory factory = new ActionFactory();
        try {
            demonstrateExecuteCommand(factory);
            demonstrateFetchAvailableActions(factory);
            demonstrateWriteToFile(factory);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demonstrateWriteToFile(ActionFactory factory)
            throws ActionExecutionException, ActionFactory.ActionNotFoundException {
        System.out.println("Demonstrating WriteToFile action:");

        Action<Boolean> writeAction = (Action<Boolean>) factory.getAction("WriteToFile");

        String content = "Hello, this is a test file content.";
        String fileName = "test_file.txt";
        String filePath = System.getProperty("java.io.tmpdir");

        ActionParams params = new ActionParams(Map.of(
                "content", content,
                "filePath", filePath,
                "fileName", fileName
        ));

        ActionResult<Boolean> result = writeAction.execute(params);

        if (result.status() == ActionStatus.SUCCESS) {
            System.out.println("File written successfully.");
            try {
                String writtenContent = Files.readString(Path.of(filePath, fileName));
                System.out.println("File content: " + writtenContent);
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to write file.");
        }

        System.out.println();
    }

    /**
     * Demonstrates the usage of the ExecuteCommand action.
     *
     * @param factory The ActionFactory to use for retrieving the action.
     * @throws ActionFactory.ActionNotFoundException If the ExecuteCommand action is not found.
     * @throws ActionExecutionException If an error occurs during action execution.
     */
    private static void demonstrateExecuteCommand(ActionFactory factory)
            throws ActionFactory.ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating ExecuteCommand action:");

        // Get the ExecuteCommand action
        Action<CommandResult> executeAction = (Action<CommandResult>) factory.getAction("ExecuteCommand");

        // Set up the parameters for the action
        ActionParams params = new ActionParams(Map.of("command", "echo Hello, Action System!"));

        // Execute the action
        ActionResult<CommandResult> result = executeAction.execute(params);

        // Handle the result
        if (result.status() == ActionStatus.SUCCESS) {
            CommandResult commandResult = result.result();
            System.out.println("Command executed: " + commandResult.command());
            System.out.println("Output: " + commandResult.output());
            System.out.println("Exit code: " + commandResult.exitCode());
        } else {
            System.out.println("Command execution failed");
        }

        System.out.println();
    }

    /**
     * Demonstrates the usage of the FetchAvailableActions action.
     *
     * @param factory The ActionFactory to use for retrieving the action.
     * @throws ActionFactory.ActionNotFoundException If the FetchAvailableActions action is not found.
     * @throws ActionExecutionException If an error occurs during action execution.
     */
    private static void demonstrateFetchAvailableActions(ActionFactory factory)
            throws ActionFactory.ActionNotFoundException, ActionExecutionException {
        System.out.println("Demonstrating FetchAvailableActions action:");

        // Get the FetchAvailableActions action
        Action<Map<String, ActionMetadata>> fetchAction =
                (Action<Map<String, ActionMetadata>>) factory.getAction("FetchAvailableActions");

        // Execute the action (no parameters needed)
        ActionResult<Map<String, ActionMetadata>> result = fetchAction.execute(new ActionParams(Map.of()));

        // Handle the result
        if (result.status() == ActionStatus.SUCCESS) {
            Map<String, ActionMetadata> availableActions = result.result();
            System.out.println("Available Actions:");
            for (Map.Entry<String, ActionMetadata> entry : availableActions.entrySet()) {
                ActionMetadata metadata = entry.getValue();
                System.out.println("- " + metadata.name() + ": " + metadata.description());
                System.out.println("  Parameters: " + metadata.expectedParams());
                System.out.println("  Return type: " + metadata.returnType().getSimpleName());
            }
        } else {
            System.out.println("Failed to fetch available actions");
        }
    }
}
