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
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadFileActionTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadFileSuccess() throws Exception {
        String content = "Hello, World!";
        Path filePath = tempDir.resolve("test.txt");
        Files.writeString(filePath, content);

        ReadFileAction action = new ReadFileAction();
        ActionParams params = new ActionParams(Map.of("filePath", filePath.toString()));

        ActionResult<String> result = action.execute(params);

        assertEquals(ActionStatus.SUCCESS, result.status());
        assertEquals(content, result.result());
    }

    @Test
    void testReadFileNonexistentFile() {
        ReadFileAction action = new ReadFileAction();
        ActionParams params = new ActionParams(Map.of("filePath", tempDir.resolve("nonexistent.txt").toString()));

        assertThrows(ActionExecutionException.class, () -> action.execute(params));
    }

    @Test
    void testReadFileNullParams() {
        ReadFileAction action = new ReadFileAction();

        assertThrows(ActionExecutionException.class, () -> action.execute(null));
    }

    @Test
    void testReadFileEmptyPath() {
        ReadFileAction action = new ReadFileAction();
        ActionParams params = new ActionParams(Map.of("filePath", ""));

        assertThrows(ActionExecutionException.class, () -> action.execute(params));
    }

    @Test
    void testGetMetadata() {
        ReadFileAction action = new ReadFileAction();
        ActionMetadata metadata = action.getMetadata();

        assertEquals("ReadFile", metadata.name());
        assertEquals("Reads the contents of a file at the specified path", metadata.description());
        assertEquals(Map.of("filePath", String.class), metadata.expectedParams());
        assertEquals(String.class, metadata.returnType());
    }
}