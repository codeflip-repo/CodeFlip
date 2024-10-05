package io.codeflip.actions;

import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecuteCommandActionTest {

    @Test
    void testExecuteCommandSuccess() throws ActionExecutionException {
        ExecuteCommandAction action = new ExecuteCommandAction();
        ActionParams params = new ActionParams(Map.of("command", "echo Hello, World!"));

        ActionResult<CommandResult> result = action.execute(params);

        assertEquals(ActionStatus.SUCCESS, result.status());
        CommandResult commandResult = result.result();
        assertEquals("echo Hello, World!", commandResult.command());
        assertEquals("Hello, World!", commandResult.output().trim());
        assertEquals(0, commandResult.exitCode());
    }

    @Test
    void testExecuteCommandFailure() throws ActionExecutionException {
        ExecuteCommandAction action = new ExecuteCommandAction();
        ActionParams params = new ActionParams(Map.of("command", "nonexistentcommand"));

        ActionResult<CommandResult> result = action.execute(params);

        assertEquals(ActionStatus.FAILURE, result.status());
        CommandResult commandResult = result.result();
        assertNotEquals(0, commandResult.exitCode());
        assertTrue(commandResult.output().toLowerCase().contains("not found") ||
                commandResult.output().toLowerCase().contains("not recognized") ||
                commandResult.output().toLowerCase().contains("cannot run program"));
    }

    @Test
    void testExecuteCommandNullCommand() {
        ExecuteCommandAction action = new ExecuteCommandAction();
        Map<String, Object> map = new HashMap<>();
        map.put("command", null);
        ActionParams params = new ActionParams(map);

        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> action.execute(params));
        assertEquals("Command parameter is missing or null", exception.getMessage());
    }

    @Test
    void testExecuteCommandMissingCommand() {
        ExecuteCommandAction action = new ExecuteCommandAction();
        ActionParams params = new ActionParams(new HashMap<>());  // Empty map

        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> action.execute(params));
        assertEquals("Command parameter is missing or null", exception.getMessage());
    }

    @Test
    void testExecuteCommandEmptyCommand() {
        ExecuteCommandAction action = new ExecuteCommandAction();
        ActionParams params = new ActionParams(Map.of("command", "  "));

        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> action.execute(params));
        assertEquals("Command cannot be empty", exception.getMessage());
    }

    @Test
    void testExecuteCommandNullParams() {
        ExecuteCommandAction action = new ExecuteCommandAction();

        ActionExecutionException exception = assertThrows(ActionExecutionException.class, () -> action.execute(null));
        assertEquals("ActionParams cannot be null", exception.getMessage());
    }

    @Test
    void testGetMetadata() {
        ExecuteCommandAction action = new ExecuteCommandAction();
        ActionMetadata metadata = action.getMetadata();

        assertEquals("ExecuteCommand", metadata.name());
        assertEquals("Executes a system command", metadata.description());
        assertEquals(Map.of("command", String.class), metadata.expectedParams());
        assertEquals(CommandResult.class, metadata.returnType());
    }
}