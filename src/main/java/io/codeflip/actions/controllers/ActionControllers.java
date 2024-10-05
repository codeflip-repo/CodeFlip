package io.codeflip.actions.controllers;

import io.codeflip.actions.ActionFactory;
import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionRequest;
import io.codeflip.actions.model.ActionResponse;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/actions")
public class ActionControllers {

    @Autowired
    private ActionFactory actionFactory;

    @PostMapping("/execute-command")
    public ResponseEntity<ActionResponse> executeCommand(@RequestBody ActionRequest request) {
        return executeAction("ExecuteCommand", request);
    }

    @PostMapping("/write-file")
    public ResponseEntity<ActionResponse> writeFile(@RequestBody ActionRequest request) {
        return executeAction("WriteToFile", request);
    }

    @PostMapping("/read-file")
    public ResponseEntity<ActionResponse> readFile(@RequestBody ActionRequest request) {
        return executeAction("ReadFile", request);
    }

    @GetMapping("/available-actions")
    public ResponseEntity<ActionResponse> fetchAvailableActions() {
        return executeAction("FetchAvailableActions", new ActionRequest(Map.of()));
    }

    private ResponseEntity<ActionResponse> executeAction(String actionName, ActionRequest request) {
        try {
            Action<?> action = actionFactory.getAction(actionName);
            ActionResult<?> result = action.execute(new ActionParams(request.params()));
            return ResponseEntity.ok(new ActionResponse(result.status(), result.result()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ActionResponse(ActionStatus.FAILURE, e.getMessage()));
        }
    }
}