package ui.repl;

import client.WebSocketFacade;
import model.GameData;
import ui.repl.clients.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL {
    private final SignedOutClient signedOutClient;
    private final SignedInClient signedInClient;
    private final GameClient gameClient;
    Client currentClientState;

    public ClientData clientData = new ClientData();
    private WebSocketFacade webSocketFacade;

    public REPL(String serverURL) {
        try {
            this.webSocketFacade = new WebSocketFacade(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         signedOutClient = new SignedOutClient(serverURL);
         signedInClient = new SignedInClient(serverURL, webSocketFacade);
         gameClient = new GameClient(serverURL);
         currentClientState = signedOutClient;
    }

    public void run() {
        printNormal("Welcome to CS240 chess. Type 'help' to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        replLoop:
        while (true) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                ClientResponse response = currentClientState.eval(line, clientData);
                switch (response.newClient()) {
                    case "signedOutClient" -> currentClientState = signedOutClient;
                    case "signedInClient" -> currentClientState = signedInClient;
                    case "gameClient" -> currentClientState = gameClient;
                    case "quit" -> {
                        break replLoop;
                    }
                    case null, default -> {}
                }

                ClientData newClientData = response.newClientData();
                if (newClientData != null) {
                    clientData = newClientData;
                }

                result = response.result();
                printNormal(result);
            } catch (Throwable throwable) {
                var msg = throwable.toString();
                printError(msg);
            }
        }
        printNewline();
    }

    public void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        System.out.print("\n[" + currentClientState.getPromptTitle() + "] >>> " + SET_TEXT_COLOR_GREEN);
    }

    private void printNormal(String text) {
        System.out.print(SET_TEXT_COLOR_BLUE + text);
    }

    private void printError(String text) {
        System.out.print(SET_TEXT_COLOR_RED + text);
    }

    private void printNewline() {
        System.out.println();
    }

    public void evaluateNotificationMessage(NotificationMessage notificationMessage) {
        String notificationText = notificationMessage.getContent();
        printNormal(notificationText);
        printNewline();
        printPrompt();
    }

    public void evaluateErrorMessage(ErrorMessage errorMessage) {
        String errorText = errorMessage.getContent();
        printError("ERROR: " + errorText);
        printNewline();
        printPrompt();
    }

    public void evaluateLoadGameMessage(LoadGameMessage loadGameMessage) {
        GameData gameData = loadGameMessage.getContent();
        clientData.setActiveGame(gameData);
        gameClient.drawBoard(clientData);
        printNewline();
        printPrompt();
    }
}
