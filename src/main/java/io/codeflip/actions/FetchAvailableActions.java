package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;

import java.util.Map;

/**
 * An Action that retrieves information about all available actions in the system.
 * This action doesn't require any input parameters and returns a map of action names
 * to their corresponding metadata.
 */
public class FetchAvailableActions implements Action<Map<String, ActionMetadata>> {

    private final ActionFactory actionFactory;

    /**
     * Constructs a new FetchAvailableActions with the given ActionFactory.
     *
     * @param actionFactory The ActionFactory to use for fetching available actions.
     */
    public FetchAvailableActions(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public ActionResult<Map<String, ActionMetadata>> execute(ActionParams params) throws ActionExecutionException {
        try {
            Map<String, ActionMetadata> availableActions = actionFactory.getAvailableActions();
            return new ActionResult<>(availableActions, ActionStatus.SUCCESS);
        } catch (Exception e) {
            throw new ActionExecutionException("Error fetching available actions", e);
        }
    }

    @Override
    public ActionMetadata getMetadata() {
        return new ActionMetadata(
                "FetchAvailableActions",
                "Retrieves information about all available actions in the system",
                Map.of(), // No input parameters required
                Map.class
        );
    }
}