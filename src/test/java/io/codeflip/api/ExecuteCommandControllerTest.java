package io.codeflip.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExecuteCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testExecuteCommand() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/executeCommand")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("echo 'Hello, World!'"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").exists())
                .andExpect(jsonPath("$.exitCode").exists())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("Response content: " + content);

        // Verify application log
        verifyLogFile("application");

        // Verify metrics log
        verifyLogFile("metrics");
    }

    private void verifyLogFile(String prefix) throws Exception {
        String logDir = "build/logs";
        File logDirFile = new File(logDir);

        System.out.println("Log directory exists: " + logDirFile.exists());
        System.out.println("Log directory is directory: " + logDirFile.isDirectory());
        System.out.println("Log directory can read: " + logDirFile.canRead());
        System.out.println("Log directory can write: " + logDirFile.canWrite());

        System.out.println("Log directory contents:");
        if (logDirFile.exists() && logDirFile.isDirectory()) {
            for (File file : logDirFile.listFiles()) {
                System.out.println(file.getName() + " - " + file.length() + " bytes");
            }
        } else {
            System.out.println("Log directory does not exist or is not a directory");
        }

        String currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
        String expectedFileName = prefix + "_" + currentHour + ".log";
        Path rolledLogFile = new File(logDir, expectedFileName).toPath();
        Path currentLogFile = new File(logDir, prefix + ".log").toPath();

        assertTrue(Files.exists(currentLogFile) || Files.exists(rolledLogFile),
                prefix + " log file should exist: either " + currentLogFile + " or " + rolledLogFile);

        String fileContent = Files.exists(currentLogFile) ? Files.readString(currentLogFile) : Files.readString(rolledLogFile);
        System.out.println(prefix + " log file content:\n" + fileContent);
        assertFalse(fileContent.isEmpty(), prefix + " log file should not be empty");

        if ("metrics".equals(prefix)) {
            assertTrue(fileContent.contains("command.execution.count"), "Metrics log should contain command execution count");
            assertTrue(fileContent.contains("command.execution.success"), "Metrics log should contain command execution success");
        } else {
            assertTrue(fileContent.contains("Received command: echo 'Hello, World!'"), "Application log should contain received command");
            assertTrue(fileContent.contains("Command executed. Exit code: 0"), "Application log should contain execution result");
        }
    }
}