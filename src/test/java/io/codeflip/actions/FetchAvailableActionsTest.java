package io.codeflip.actions;

import io.codeflip.actions.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class FetchAvailableActionsTest {

    private ActionFactory actionFactory;
    private FetchAvailableActions fetchAction;

    @BeforeEach
    void setUp() {
        actionFactory = new ActionFactory();
        fetchAction = new FetchAvailableActions(actionFactory);
        actionFactory.registerAction(fetchAction);
    }

    @Test
    void testFetchAvailableActions() throws ActionExecutionException {
        ActionResult<Map<String, ActionMetadata>> result = fetchAction.execute(new ActionParams(Map.of()));

        assertEquals(ActionStatus.SUCCESS, result.status());
        Map<String, ActionMetadata> availableActions = result.result();

        assertTrue(availableActions.containsKey("ExecuteCommand"));
        assertTrue(availableActions.containsKey("FetchAvailableActions"));

        ActionMetadata executeCommandMetadata = availableActions.get("ExecuteCommand");
        assertEquals("ExecuteCommand", executeCommandMetadata.name());
        assertEquals("Executes a system command", executeCommandMetadata.description());

        ActionMetadata fetchAvailableActionsMetadata = availableActions.get("FetchAvailableActions");
        assertEquals("FetchAvailableActions", fetchAvailableActionsMetadata.name());
        assertEquals("Retrieves information about all available actions in the system", fetchAvailableActionsMetadata.description());
    }

    @Test
    void testGetMetadata() {
        ActionMetadata metadata = fetchAction.getMetadata();

        assertEquals("FetchAvailableActions", metadata.name());
        assertEquals("Retrieves information about all available actions in the system", metadata.description());
        assertTrue(metadata.expectedParams().isEmpty());
        assertEquals(Map.class, metadata.returnType());
    }
}