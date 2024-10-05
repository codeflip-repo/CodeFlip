package io.codeflip.actions;

import io.codeflip.actions.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class WriteToFileAction implements Action<Boolean> {

    @Override
    public ActionResult<Boolean> execute(ActionParams params) throws ActionExecutionException {
        String content = (String) params.get("content");
        String filePath = (String) params.get("filePath");

        if (content == null || filePath == null) {
            throw new ActionExecutionException("Missing required parameters: content or filePath");
        }

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path.getParent())) {
                throw new ActionExecutionException("Directory does not exist: " + path.getParent());
            }
            Files.writeString(path, content);
            return new ActionResult<>(true, ActionStatus.SUCCESS);
        } catch (Exception e) {
            throw new ActionExecutionException("Error writing to file: " + e.getMessage(), e);
        }
    }

    @Override
    public ActionMetadata getMetadata() {
        return new ActionMetadata(
                "WriteToFile",
                "Writes content to a file at the specified path",
                Map.of(
                        "content", String.class,
                        "filePath", String.class
                ),
                Boolean.class
        );
    }
}