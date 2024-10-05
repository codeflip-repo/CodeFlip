package io.codeflip.actions;

import io.codeflip.actions.model.Action;
import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * An Action that writes content to a file at a specified path.
 */
public class WriteToFileAction implements Action<Boolean> {

    @Override
    public ActionResult<Boolean> execute(ActionParams params) throws ActionExecutionException {
        String content = (String) params.get("content");
        String filePath = (String) params.get("filePath");
        String fileName = (String) params.get("fileName");

        if (content == null || filePath == null || fileName == null) {
            throw new ActionExecutionException("Missing required parameters: content, filePath, or fileName");
        }

        try {
            Path path = Paths.get(filePath, fileName);
            Files.createDirectories(path.getParent());
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
                        "filePath", String.class,
                        "fileName", String.class
                ),
                Boolean.class
        );
    }
}