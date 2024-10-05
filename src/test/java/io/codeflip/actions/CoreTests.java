package io.codeflip.actions;

import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class CoreTests {

    @Test
    void testActionParams() {
        Map<String, Object> params = Map.of("key1", "value1", "key2", 42);
        ActionParams actionParams = new ActionParams(params);

        assertEquals("value1", actionParams.get("key1"));
        assertEquals(42, actionParams.get("key2"));
        assertNull(actionParams.get("nonexistent"));
    }

    @Test
    void testActionResult() {
        String result = "Test Result";
        ActionResult<String> successResult = new ActionResult<>(result, ActionStatus.SUCCESS);
        ActionResult<String> failureResult = new ActionResult<>(result, ActionStatus.FAILURE);

        assertEquals(result, successResult.result());
        assertEquals(ActionStatus.SUCCESS, successResult.status());
        assertEquals(result, failureResult.result());
        assertEquals(ActionStatus.FAILURE, failureResult.status());
    }

    @Test
    void testActionMetadata() {
        String name = "TestAction";
        String description = "This is a test action";
        Map<String, Class<?>> expectedParams = Map.of("param1", String.class, "param2", Integer.class);
        Class<?> returnType = String.class;

        ActionMetadata metadata = new ActionMetadata(name, description, expectedParams, returnType);

        assertEquals(name, metadata.name());
        assertEquals(description, metadata.description());
        assertEquals(expectedParams, metadata.expectedParams());
        assertEquals(returnType, metadata.returnType());
    }
}
