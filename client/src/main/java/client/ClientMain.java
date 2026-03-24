package client;

import chess.*;
import ui.repl.REPL;

public class ClientMain {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1) {
            serverURL = args[0];
        }
        REPL repl = new REPL(serverURL);
        repl.run();
    }
}
