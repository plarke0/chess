package model;

import chess.ChessGame;

record GameData(int gameTD, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}