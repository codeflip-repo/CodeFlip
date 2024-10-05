package io.codeflip.actions;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class ExecuteCommandActionTest {

    @Test
    void testExecute() throws Exception {
        ExecuteCommandAction action = new ExecuteCommandAction(Collections.singletonList("echo Hello, World!"));
        String result = action.execute();
        assertTrue(result.contains("Hello, World!"));
        assertTrue(result.contains("exited with code 0"));
    }

    @Test
    void testGetName() {
        ExecuteCommandAction action = new ExecuteCommandAction(Collections.emptyList());
        assertEquals("ExecuteCommand", action.getName());
    }
}