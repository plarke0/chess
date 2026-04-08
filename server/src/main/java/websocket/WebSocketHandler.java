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
import websocket.messages.LoadGameMessage;

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
        try {
            UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
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

    private ChessGame getGame(String authToken, int gameID) throws ResponseException, DataAccessException {
        checkAuth(authToken);

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new ResponseException(400, "bad request");
        }

        return gameData.game();
    }

    private void connect(String authToken, int gameID, Session rootSession) throws ResponseException, DataAccessException, IOException {
        ChessGame game = getGame(authToken, gameID);
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, game);
        connectionManager.broadcastToGameExclusive(rootSession, gameID, loadGameMessage);


        // Send a NotificationMessage to all other clients saying the user has joined (observer or player + color)
    }
}
