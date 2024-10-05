package io.codeflip.actions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FetchAvailableActionsActionTest {

    @Test
    void testExecute() throws Exception {
        FetchAvailableActionsAction action = new FetchAvailableActionsAction();
        String result = action.execute();
        assertTrue(result.contains("ExecuteCommand"));
        assertTrue(result.contains("FetchAvailableActions"));
    }

    @Test
    void testGetName() {
        FetchAvailableActionsAction action = new FetchAvailableActionsAction();
        assertEquals("FetchAvailableActions", action.getName());
    }
}
