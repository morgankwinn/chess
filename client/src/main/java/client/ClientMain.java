package client;

import chess.*;
import ui.PreLoginClient;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        PreLoginClient client = new PreLoginClient("http://localhost:8080");
        client.run();
    }
}
