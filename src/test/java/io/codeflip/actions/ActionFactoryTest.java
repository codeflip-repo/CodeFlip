package io.codeflip.actions;

import io.codeflip.actions.ActionFactory.ActionNotFoundException;
import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActionFactoryTest {

    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        actionFactory = new ActionFactory();
    }

    @Test
    void testGetExistingAction() throws ActionNotFoundException {
        Action<?> action = actionFactory.getAction("ExecuteCommand");
        assertNotNull(action);
        assertTrue(action instanceof ExecuteCommandAction);
    }

    @Test
    void testGetNonexistentAction() {
        assertThrows(ActionNotFoundException.class, () -> actionFactory.getAction("NonexistentAction"));
    }

    @Test
    void testRegisterNewAction() throws ActionNotFoundException {
        FetchAvailableActions newAction = new FetchAvailableActions(actionFactory);
        actionFactory.registerAction(newAction);

        Action<?> retrievedAction = actionFactory.getAction("FetchAvailableActions");
        assertNotNull(retrievedAction);
        assertTrue(retrievedAction instanceof FetchAvailableActions);
    }

    @Test
    void testGetAvailableActions() {
        Map<String, ActionMetadata> availableActions = actionFactory.getAvailableActions();

        assertTrue(availableActions.containsKey("ExecuteCommand"));
        ActionMetadata metadata = availableActions.get("ExecuteCommand");
        assertEquals("ExecuteCommand", metadata.name());
        assertEquals("Executes a system command", metadata.description());
    }
}