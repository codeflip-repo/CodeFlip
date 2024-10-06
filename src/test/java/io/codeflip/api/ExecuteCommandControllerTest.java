package io.codeflip.api;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExecuteCommandControllerTest {

    private ExecuteCommandController controller;

    @BeforeEach
    void setUp() {
        controller = new ExecuteCommandController(new SimpleMeterRegistry());
    }

    @Test
    void testExecuteCommand() {
        ResponseEntity<?> response = controller.executeCommand("echo Hello, CodeFlip!");
        assertTrue(response.getBody() instanceof ExecuteCommandController.CommandResult);
        ExecuteCommandController.CommandResult result = (ExecuteCommandController.CommandResult) response.getBody();
        assertNotNull(result);
        assertTrue(result.getOutput().contains("Hello, CodeFlip!"));
        assertEquals(0, result.getExitCode());
    }

    @Test
    void testExecuteInvalidCommand() {
        ResponseEntity<?> response = controller.executeCommand("invalidcommand");
        assertTrue(response.getBody() instanceof ExecuteCommandController.CommandResult);
        ExecuteCommandController.CommandResult result = (ExecuteCommandController.CommandResult) response.getBody();
        assertNotNull(result);
        assertFalse(result.getOutput().isEmpty());
        assertNotEquals(0, result.getExitCode());
    }

    @Test
    void testExecuteCommandWithInvalidDirectory() {
        ResponseEntity<?> response = controller.executeCommand("ls /this_directory_does_not_exist");
        assertTrue(response.getBody() instanceof ExecuteCommandController.CommandResult);
        ExecuteCommandController.CommandResult result = (ExecuteCommandController.CommandResult) response.getBody();
        assertNotNull(result);
        assertFalse(result.getOutput().isEmpty());
        assertTrue(result.getOutput().toLowerCase().contains("no such file or directory"));
        assertNotEquals(0, result.getExitCode());
    }
}
