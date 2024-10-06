package io.codeflip.api;

import io.codeflip.api.logging.MetricsLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class ExecuteCommandController {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandController.class);
    private final MetricsLogger metricsLogger = new MetricsLogger();

    @PostMapping("/api/executeCommand")
    @Operation(summary = "Execute a command", description = "Executes a given command and returns the output and exit code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Command executed",
                    content = @Content(schema = @Schema(implementation = CommandResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid command provided",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> executeCommand(@RequestBody String command) {
        logger.info("Received command: {}", command);
        metricsLogger.logMetric("command.execution.count", 1);

        if (command == null || command.trim().isEmpty()) {
            logger.warn("Invalid command received");
            metricsLogger.logMetric("command.execution.error", 1);
            return ResponseEntity.badRequest().body("Error: Command cannot be empty");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", command);
            processBuilder.redirectErrorStream(true);  // Redirect stderr to stdout
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            logger.info("Command executed. Exit code: {}", exitCode);
            String result = output.toString();
            logger.info("Command output: {}", result);
            metricsLogger.logMetric("command.execution.success", 1);
            return ResponseEntity.ok(new CommandResult(result, exitCode));
        } catch (Exception e) {
            logger.error("Error executing command", e);
            metricsLogger.logMetric("command.execution.error", 1);
            return ResponseEntity.ok(new CommandResult("Error: " + e.getMessage(), -1));
        }
    }

    public static class CommandResult {
        private final String output;
        private final int exitCode;

        public CommandResult(String output, int exitCode) {
            this.output = output;
            this.exitCode = exitCode;
        }

        public String getOutput() {
            return output;
        }

        public int getExitCode() {
            return exitCode;
        }
    }
}