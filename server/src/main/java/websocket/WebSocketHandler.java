package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import responses.ResponseException;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final MySQLGameDAO gameDAO = new MySQLGameDAO();
    private final MySQLAuthDAO authDAO = new MySQLAuthDAO();

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {
        System.out.println("Websocket connected");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws ResponseException, DataAccessException{
        UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> {
                    connect(command.getAuthToken(), command.getGameID(), wsMessageContext.session);
                }
                case MAKE_MOVE -> {
                    // Verify validity
                    // Update game
                    // Send LoadGameMessage to all clients in the game with updated game
                    // Send NotificationMessage to all other clients with the move just made
                    // If the move results in check, checkmate, or stalemate, sed a NotificationMessage to all clients
                }
                case LEAVE -> {
                    // Update game to remove root client
                    // Send NotificationMessage to all other clients
                }
                case RESIGN -> {
                    // Mark the game as over and update it
                    // Sends a NotificationMessage to all clients
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ResponseException | DataAccessException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, ex.getMessage());
            try {
                connectionManager.broadcastToGameIndividual(wsMessageContext.session, command.getGameID(), errorMessage);
            } catch (IOException e) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {
        System.out.println("Websocket closed");
    }

    private void checkAuth(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }
    }

    private GameData getGameData(String authToken, int gameID) throws ResponseException, DataAccessException {
        checkAuth(authToken);

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new ResponseException(400, "bad request");
        }

        return gameData;
    }

    private String getUsername(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(400, "bad request");
        }

        return authData.username();
    }

    @NotNull
    private static String getConnectMessage(String username, GameData gameData) {
        ChessGame.TeamColor color = null;
        if (username.equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        }
        String message;
        if (color != null) {
            message = "User " + username + " has joined as player " + (color == ChessGame.TeamColor.WHITE ? "White." : "Black.");
        } else {
            message = "User " + username + " has joined as an observer.";
        }
        return message;
    }

    private void connect(String authToken, int gameID, Session rootSession) throws ResponseException, DataAccessException, IOException {
        connectionManager.add(rootSession, gameID);
        GameData gameData = getGameData(authToken, gameID);
        ChessGame game = gameData.game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, game);
        connectionManager.broadcastToGameIndividual(rootSession, gameID, loadGameMessage);

        String username = getUsername(authToken);
        String message = getConnectMessage(username, gameData);
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connectionManager.broadcastToGameExclusive(rootSession, gameID, notificationMessage);
    }
}
