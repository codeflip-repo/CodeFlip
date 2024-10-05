# CodeFlip Action System

## Overview

The CodeFlip Action System is a flexible and extensible framework for defining and executing various actions in a Java environment. It provides a standardized way to create, manage, and execute actions with proper error handling and metadata support.

## Project Structure

```
io.codeflip.actions/
├── model/
│   ├── Action.java
│   ├── ActionFactory.java
│   ├── ActionParams.java
│   ├── ActionResult.java
│   ├── ActionStatus.java
│   ├── ActionMetadata.java
│   └── ActionExecutionException.java
├── ExecuteCommandAction.java
├── WriteToFileAction.java
├── ReadFileAction.java
└── FetchAvailableActionsAction.java
```

## Key Components

1. `Action<T>` Interface: The core interface that all actions must implement.
2. `ActionFactory`: Manages the creation and retrieval of action instances.
3. `ActionParams`: Encapsulates the parameters passed to an action.
4. `ActionResult<T>`: Represents the result of an action execution.
5. `ActionMetadata`: Provides metadata about an action.
6. `ActionStatus`: Enum representing the status of an action execution.
7. `ActionExecutionException`: Custom exception for action execution errors.

All these components are located in the `io.codeflip.actions.model` package.

## Implemented Actions

1. `ExecuteCommandAction`: Executes a system command and returns the result.
2. `WriteToFileAction`: Writes content to a file at a specified path.
3. `FetchAvailableActionsAction`: Retrieves information about all available actions.
4. `ReadFileAction`: Reads the contents of a file at the specified path and returns it as a string.

These action implementations are located directly in the `io.codeflip.actions` package.

## Usage

Here's a basic example of how to use the Action System:

```java
import io.codeflip.actions.model.*;
import io.codeflip.actions.ExecuteCommandAction;

ActionFactory factory = new ActionFactory();
Action<CommandResult> action = (Action<CommandResult>) factory.getAction("ExecuteCommand");

ActionParams params = new ActionParams(Map.of("command", "echo Hello, World!"));
ActionResult<CommandResult> result = action.execute(params);

if (result.status() == ActionStatus.SUCCESS) {
    CommandResult commandResult = result.result();
    System.out.println("Output: " + commandResult.output());
} else {
    System.out.println("Command execution failed");
}
```

## Adding New Actions

To add a new action:

1. Create a new class implementing the `Action<T>` interface in the `io.codeflip.actions` package.
2. Implement the `execute` and `getMetadata` methods.
3. Register the new action in the `ActionFactory` constructor.

## Running Tests

To run the tests, use the following Maven command:

```
mvn test
```

## Dependencies

- Java 17 or higher
- JUnit 5 for testing


## License

This project is not licensed for use, modification, or distribution. All rights are reserved. This code is provided for educational and demonstration purposes only. Any use, reproduction, or distribution of the code without explicit permission is strictly prohibited.