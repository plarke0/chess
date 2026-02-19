package model;

import chess.ChessGame;

public record GameData(int gameTD, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}