package service.requests;

public record JoinGameRequest(String gameID, String playerColor, String authToken) {}
