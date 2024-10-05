package io.codeflip.actions.model;

/**
 * Represents the result of executing a system command.
 *
 * @param command The command that was executed.
 * @param output The output produced by the command.
 * @param exitCode The exit code returned by the command.
 */
public record CommandResult(String command, String output, int exitCode) {}
