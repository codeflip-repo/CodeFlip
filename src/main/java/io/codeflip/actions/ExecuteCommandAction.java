package io.codeflip.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * ExecuteCommandAction is responsible for executing system commands.
 */
public class ExecuteCommandAction implements Action {

    private final List<String> commands;

    /**
     * Constructs an ExecuteCommandAction with the specified commands.
     *
     * @param commands A list of commands to be executed.
     */
    public ExecuteCommandAction(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public String execute() throws Exception {
        StringBuilder output = new StringBuilder();
        for (String command : commands) {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            output.append("Command '").append(command).append("' exited with code ").append(exitCode).append("\n");
        }
        return output.toString();
    }

    @Override
    public String getName() {
        return "ExecuteCommand";
    }
}