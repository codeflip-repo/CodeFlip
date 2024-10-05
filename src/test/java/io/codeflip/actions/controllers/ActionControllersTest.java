package io.codeflip.actions.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codeflip.actions.ActionFactory;
import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionRequest;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import io.codeflip.actions.model.CommandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for ActionControllers.
 * This class tests the REST endpoints for various actions.
 */
@WebMvcTest(ActionControllers.class)
class ActionControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionFactory actionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Action<?>> mockActions;

    /**
     * Sets up the test environment before each test.
     * Initializes mock actions and configures the actionFactory mock.
     */
    @BeforeEach
    void setUp() throws ActionFactory.ActionNotFoundException {
        mockActions = new HashMap<>();
        when(actionFactory.getAction(any())).thenAnswer(invocation -> {
            String actionName = invocation.getArgument(0);
            if (!mockActions.containsKey(actionName)) {
                throw new ActionFactory.ActionNotFoundException("Action not found: " + actionName);
            }
            return mockActions.get(actionName);
        });
    }

    /**
     * Tests the execute command endpoint.
     */
    @Test
    void testExecuteCommand() throws Exception {
        CommandResult commandResult = new CommandResult("echo Hello", "Hello", 0);
        setupMockAction("ExecuteCommand", commandResult);

        mockMvc.perform(post("/api/actions/execute-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ActionRequest(Map.of("command", "echo Hello")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.result.command").value("echo Hello"))
                .andExpect(jsonPath("$.result.output").value("Hello"))
                .andExpect(jsonPath("$.result.exitCode").value(0));
    }

    /**
     * Tests the write file endpoint.
     */
    @Test
    void testWriteFile() throws Exception {
        setupMockAction("WriteToFile", true);

        mockMvc.perform(post("/api/actions/write-file")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ActionRequest(Map.of("content", "Hello", "filePath", "/tmp/test.txt")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value(true));
    }

    /**
     * Tests the read file endpoint.
     */
    @Test
    void testReadFile() throws Exception {
        setupMockAction("ReadFile", "File content");

        mockMvc.perform(post("/api/actions/read-file")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ActionRequest(Map.of("filePath", "/tmp/test.txt")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value("File content"));
    }

    /**
     * Tests the fetch available actions endpoint.
     */
    @Test
    void testFetchAvailableActions() throws Exception {
        Map<String, ActionMetadata> availableActions = Map.of(
                "TestAction", new ActionMetadata("TestAction", "Test action description", Map.of(), String.class)
        );
        setupMockAction("FetchAvailableActions", availableActions);

        mockMvc.perform(get("/api/actions/available-actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.result.TestAction.name").value("TestAction"))
                .andExpect(jsonPath("$.result.TestAction.description").value("Test action description"));
    }

    /**
     * Tests the behavior when an action is not found.
     */
    @Test
    void testActionNotFound() throws Exception {
        mockMvc.perform(post("/api/actions/execute-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ActionRequest(Map.of()))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.result").value("Action not found: ExecuteCommand"));
    }

    /**
     * Sets up a mock action with the given name and result.
     *
     * @param actionName The name of the action to mock.
     * @param result The result the mock action should return.
     */
    private <T> void setupMockAction(String actionName, T result) throws ActionExecutionException {
        Action<T> mockAction = mock(Action.class);
        when(mockAction.execute(any())).thenReturn(new ActionResult<>(result, ActionStatus.SUCCESS));
        mockActions.put(actionName, mockAction);
    }
}