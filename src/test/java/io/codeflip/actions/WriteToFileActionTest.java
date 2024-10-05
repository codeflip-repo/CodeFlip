package io.codeflip.actions;

import io.codeflip.actions.model.ActionExecutionException;
import io.codeflip.actions.model.ActionMetadata;
import io.codeflip.actions.model.ActionParams;
import io.codeflip.actions.model.ActionResult;
import io.codeflip.actions.model.ActionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriteToFileActionTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteToFileSuccess() throws Exception {
        WriteToFileAction action = new WriteToFileAction();
        String content = "Hello, World!";
        Path filePath = tempDir.resolve("test.txt");

        ActionParams params = new ActionParams(Map.of(
                "content", content,
                "filePath", filePath.toString()
        ));

        ActionResult<Boolean> result = action.execute(params);

        assertEquals(ActionStatus.SUCCESS, result.status());
        assertTrue(result.result());
        assertTrue(Files.exists(filePath));
        assertEquals(content, Files.readString(filePath));
    }

    @Test
    void testWriteToFileInNonexistentDirectory() {
        WriteToFileAction action = new WriteToFileAction();
        String content = "Hello, World!";
        Path filePath = tempDir.resolve("nonexistent").resolve("test.txt");

        ActionParams params = new ActionParams(Map.of(
                "content", content,
                "filePath", filePath.toString()
        ));

        assertThrows(ActionExecutionException.class, () -> action.execute(params));
        assertFalse(Files.exists(filePath));
    }

    @Test
    void testWriteToFileMissingContent() {
        WriteToFileAction action = new WriteToFileAction();
        ActionParams params = new ActionParams(Map.of(
                "filePath", tempDir.resolve("test.txt").toString()
        ));

        assertThrows(ActionExecutionException.class, () -> action.execute(params));
    }

    @Test
    void testWriteToFileMissingFilePath() {
        WriteToFileAction action = new WriteToFileAction();
        ActionParams params = new ActionParams(Map.of(
                "content", "Hello, World!"
        ));

        assertThrows(ActionExecutionException.class, () -> action.execute(params));
    }

    @Test
    void testGetMetadata() {
        WriteToFileAction action = new WriteToFileAction();
        ActionMetadata metadata = action.getMetadata();

        assertEquals("WriteToFile", metadata.name());
        assertEquals("Writes content to a file at the specified path", metadata.description());
        assertEquals(Map.of(
                "content", String.class,
                "filePath", String.class
        ), metadata.expectedParams());
        assertEquals(Boolean.class, metadata.returnType());
    }
}