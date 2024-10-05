package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class ExecuteCommandAction implements Action<CommandResult> {

    @Override
    public ActionResult<CommandResult> execute(ActionParams params) throws ActionExecutionException {
        if (params == null) {
            throw new ActionExecutionException("ActionParams cannot be null");
        }

        Object commandObj = params.get("command");
        if (commandObj == null) {
            throw new ActionExecutionException("Command parameter is missing or null");
        }

        String command = commandObj.toString().trim();
        if (command.isEmpty()) {
            throw new ActionExecutionException("Command cannot be empty");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command.split("\\s+")));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            CommandResult result = new CommandResult(command, output.toString().trim(), exitCode);
            return new ActionResult<>(result, exitCode == 0 ? ActionStatus.SUCCESS : ActionStatus.FAILURE);
        } catch (Exception e) {
            return new ActionResult<>(new CommandResult(command, e.getMessage(), -1), ActionStatus.FAILURE);
        }
    }

    @Override
    public ActionMetadata getMetadata() {
        return new ActionMetadata(
                "ExecuteCommand",
                "Executes a system command",
                Map.of("command", String.class),
                CommandResult.class
        );
    }
}

record CommandResult(String command, String output, int exitCode) {}