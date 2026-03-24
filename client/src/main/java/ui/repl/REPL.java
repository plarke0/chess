package ui.repl;

import ui.repl.clients.*;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL {
    private final SignedOutClient signedOutClient;
    private final SignedInClient signedInClient;
    private final GameClient gameClient;
    Client currentClient;

    public REPL(String serverURL) {
         signedOutClient = new SignedOutClient(serverURL);
         signedInClient = new SignedInClient(serverURL);
         gameClient = new GameClient(serverURL);
         currentClient = signedOutClient;
    }

    public void run() {
        System.out.println("Welcome to CS240 chess. Type 'help' to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        replLoop:
        while (true) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                ClientResponse response = currentClient.eval(line);
                switch (response.newClient()) {
                    case "signedOutClient" -> currentClient = signedOutClient;
                    case "signedInClient" -> currentClient = signedInClient;
                    case "gameClient" -> currentClient = gameClient;
                    case "quit" -> {
                        break replLoop;
                    }
                    case null, default -> {}
                }
                result = response.result();
                System.out.print(result);
            } catch (Throwable throwable) {
                var msg = throwable.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        System.out.print("\n[" + currentClient.getPromptTitle() + "] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
