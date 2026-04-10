package ui.repl.clients;

import chess.ChessPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void validatePositionString(String position) throws IllegalArgumentException {
        if (position.length() != 2) {
            throw new IllegalArgumentException(
                    "'" + position + "' is not a valid chess position"
            );
        }

        Pattern pattern = Pattern.compile("[abcdefgh][12345678]");
        Matcher matcher = pattern.matcher(position);
        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "'" + position + "' is not a valid chess position"
            );
        }
    }

    public static ChessPosition decryptChessPosition(String position) {
        char rowChar = position.charAt(1);
        char columnChar = position.charAt(0);

        int rowInt = rowChar - '1' + 1;
        int columnInt = columnChar - 'a' + 1;

        return new ChessPosition(rowInt, columnInt);
    }
}
