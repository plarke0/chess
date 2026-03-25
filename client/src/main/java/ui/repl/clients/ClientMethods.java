package ui.repl.clients;

public class ClientMethods {
    public static ClientResponse unrecognisedCommand(String command) throws IllegalArgumentException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("No command was provided. Type 'help' for a list of available commands");
        } else {
            throw new IllegalArgumentException("'" + command + "' is not a valid command. Type 'help' for a list of available commands");
        }
    }

    public static void validateCommand(String[] params, int argCount) throws IllegalArgumentException {
        if (params.length < argCount) {
            throw new IllegalArgumentException(
                    "Only " + params.length
                            + (((params.length == 1)) ? " argument was given when " : " arguments were given when ")
                            + argCount + " "
                            + ((argCount == 1) ? "was" : "were") + " needed"
            );
        }
    }
}
