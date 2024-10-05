package io.codeflip.actions;

import io.codeflip.actions.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ReadFileAction implements Action<String> {

    @Override
    public ActionResult<String> execute(ActionParams params) throws ActionExecutionException {
        if (params == null) {
            throw new ActionExecutionException("ActionParams cannot be null");
        }

        String filePath = (String) params.get("filePath");
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ActionExecutionException("File path cannot be null or empty");
        }

        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            return new ActionResult<>(content, ActionStatus.SUCCESS);
        } catch (Exception e) {
            throw new ActionExecutionException("Error reading file: " + e.getMessage(), e);
        }
    }

    @Override
    public ActionMetadata getMetadata() {
        return new ActionMetadata(
                "ReadFile",
                "Reads the contents of a file at the specified path",
                Map.of("filePath", String.class),
                String.class
        );
    }
}