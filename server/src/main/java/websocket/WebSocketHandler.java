package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import responses.ResponseException;
import websocket.commands.MakeMoveCommand;
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
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) {
        UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> {
                    connect(command.getAuthToken(), command.getGameID(), wsMessageContext.session);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                    ChessMove move = makeMoveCommand.move;
                    makeMove(command.getAuthToken(), command.getGameID(), move, wsMessageContext.session);
                }
                case LEAVE -> {
                    leave(command.getAuthToken(), command.getGameID(), wsMessageContext.session);
                }
                case RESIGN -> {
                    resign(command.getAuthToken(), command.getGameID(), wsMessageContext.session);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ResponseException | DataAccessException | InvalidMoveException | IllegalStateException ex) {
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
    private static String constructConnectMessage(String username, GameData gameData) {
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

        checkAuth(authToken);

        GameData gameData = getGameData(authToken, gameID);
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, gameData);
        connectionManager.broadcastToGameIndividual(rootSession, gameID, loadGameMessage);

        String username = getUsername(authToken);
        String message = constructConnectMessage(username, gameData);
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connectionManager.broadcastToGameExclusive(rootSession, gameID, notificationMessage);
    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session rootSession)
            throws ResponseException, DataAccessException, IOException, InvalidMoveException {
        checkAuth(authToken);
        // Verify validity
        GameData gameData = getGameData(authToken, gameID);
        String username = getUsername(authToken);
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        ChessGame.TeamColor currentColor = gameData.game().getTeamTurn();
        System.out.println(username + ": " + currentColor);
        if (username.equals(white) && currentColor != ChessGame.TeamColor.WHITE) {
            throw new InvalidMoveException("It is not your turn to move");
        }
        if (username.equals(black) && currentColor != ChessGame.TeamColor.BLACK) {
            throw new InvalidMoveException("It is not your turn to move");
        }
        if (!username.equals(white) && !username.equals(black)) {
            throw new InvalidMoveException("Observers cannot move");
        }

        // Update game
        gameData.game().makeMove(move);
        gameDAO.updateGame(gameData);

        // Send LoadGameMessage to all clients in the game with updated game
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, gameData);
        connectionManager.broadcastToGame(gameID, loadGameMessage);

        // Send NotificationMessage to all other clients with the move just made
        String sourceLabel = getPositionString(move.getStartPosition());
        String destinationLabel = getPositionString(move.getEndPosition());
        String message = "Player " + username + " moved from " + sourceLabel + " to " + destinationLabel;
        NotificationMessage notification = new NotificationMessage(NOTIFICATION, message);
        connectionManager.broadcastToGameExclusive(rootSession, gameID, notification);

        // If the move results in check, checkmate, or stalemate, send a NotificationMessage to all clients
        currentColor = gameData.game().getTeamTurn();
        String extraMessage = null;
        if (gameData.game().isInCheck(currentColor)) {
            extraMessage = username + " has put their opponent in check";
        }
        if (gameData.game().isInCheckmate(currentColor)) {
            extraMessage = username + " has put their opponent in checkmate. Game over";
            gameData.game().endGame();
            gameDAO.updateGame(gameData);
        }
        if (gameData.game().isInStalemate(currentColor)) {
            extraMessage = username + " has put their opponent in stalemate. Game over";
            gameData.game().endGame();
            gameDAO.updateGame(gameData);
        }
        if (extraMessage != null) {
            NotificationMessage extraNotification = new NotificationMessage(NOTIFICATION, extraMessage);
            connectionManager.broadcastToGame(gameID, extraNotification);
        }
    }

    private void leave(String authToken, int gameID, Session rootSession) throws ResponseException, DataAccessException, IOException {
        checkAuth(authToken);
        // Update game to remove root client
        String username = getUsername(authToken);
        GameData gameData = getGameData(authToken, gameID);
        if (username.equals(gameData.whiteUsername())) {
            gameData = new GameData(
                    gameData.gameID(),
                    null,
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()
            );
        } else if (username.equals(gameData.blackUsername())) {
            gameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    null,
                    gameData.gameName(),
                    gameData.game()
            );
        }
        gameDAO.updateGame(gameData);
        connectionManager.remove(rootSession);
        // Send NotificationMessage to all other clients
        String message = username + " has left the game";
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connectionManager.broadcastToGameExclusive(rootSession, gameID, notificationMessage);
    }

    private void resign(String authToken, int gameID, Session rootSession)
            throws ResponseException, DataAccessException, IOException, IllegalStateException {
        checkAuth(authToken);
        String username = getUsername(authToken);
        GameData gameData = getGameData(authToken, gameID);
        if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
            throw new ResponseException(400, "bad request");
        }
        if (gameData.game().getTeamTurn() == ChessGame.TeamColor.GAMEOVER) {
            throw new IllegalStateException("Cannot resign, the game is over");
        }
        // Mark the game as over and update it
        gameData.game().endGame();
        gameDAO.updateGame(gameData);
        // Sends a NotificationMessage to all clients
        String message = username + " has resigned";
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connectionManager.broadcastToGame(gameID, notificationMessage);
    }

    private String getPositionString(ChessPosition position) {
        String[] columnLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int row = position.getRow();
        int columnIndex = position.getColumn() - 1;
        return row + columnLabels[columnIndex];
    }
}
