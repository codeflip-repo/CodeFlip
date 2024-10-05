package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for ActionFactory.
 * This class tests the functionality of the ActionFactory, including
 * action registration, retrieval, and listing available actions.
 */
class ActionFactoryTest {

    private ActionFactory actionFactory;

    /**
     * Sets up the test environment before each test.
     * Initializes a new ActionFactory instance.
     */
    @BeforeEach
    void setUp() {
        actionFactory = new ActionFactory();
    }

    /**
     * Tests retrieving an existing action from the factory.
     */
    @Test
    void testGetExistingAction() throws ActionFactory.ActionNotFoundException {
        Action<?> action = actionFactory.getAction("ExecuteCommand");
        assertNotNull(action);
        assertTrue(action instanceof ExecuteCommandAction);
    }

    /**
     * Tests the behavior when attempting to retrieve a non-existent action.
     */
    @Test
    void testGetNonexistentAction() {
        assertThrows(ActionFactory.ActionNotFoundException.class, () -> actionFactory.getAction("NonexistentAction"));
    }

    /**
     * Tests registering a new action and then retrieving it.
     */
    @Test
    void testRegisterNewAction() throws ActionFactory.ActionNotFoundException {
        Action<?> newAction = new FetchAvailableActions(actionFactory);
        actionFactory.registerAction(newAction);

        Action<?> retrievedAction = actionFactory.getAction("FetchAvailableActions");
        assertNotNull(retrievedAction);
        assertTrue(retrievedAction instanceof FetchAvailableActions);
    }

    /**
     * Tests retrieving metadata for all available actions.
     */
    @Test
    void testGetAvailableActions() {
        Map<String, ActionMetadata> availableActions = actionFactory.getAvailableActions();
        assertFalse(availableActions.isEmpty());
        assertTrue(availableActions.containsKey("ExecuteCommand"));
        assertTrue(availableActions.containsKey("WriteToFile"));
        assertTrue(availableActions.containsKey("ReadFile"));
        assertTrue(availableActions.containsKey("FetchAvailableActions"));
    }
}