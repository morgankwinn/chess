package ui;

import chess.ChessGame;
import model.Game;
import model.result.ListGamesResult;

import java.util.Objects;
import java.util.Scanner;

public class LoginClient {
    static ChessGame.TeamColor playerColor = null;

    public void run() {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("Logout successful")) {
            PreLoginClient.printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if (isEnteringGame(result)) {
                    GameplayClient gameplayClient = new GameplayClient();
                    gameplayClient.run();
                }
            } catch (Exception e) {
                PreLoginClient.notify(e.getMessage());
            }
        }
    }

    private String eval(String input) throws RuntimeException {
        try {
            return switch (input.toLowerCase()) {
                case "logout" -> logout();
                case "creategame" -> createGame();
                case "listgames" -> listGames();
                case "playgame" -> playGame();
                case "observegame" -> observeGame();
                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String logout() throws RuntimeException {
        try {
            PreLoginClient.server.logout(PreLoginClient.authToken);
            return "Logout successful";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String createGame() throws RuntimeException {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please provide your new game name");
            PreLoginClient.printPrompt();
            String gameName = scanner.nextLine();

            PreLoginClient.server.createGame(PreLoginClient.authToken, gameName);
            return "New game: " + gameName + " created";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String listGames() throws RuntimeException {
        try {
            ListGamesResult listGamesResult = PreLoginClient.server.listGames(PreLoginClient.authToken);
            String games = "";
            int i = 1;
            for (Game game : listGamesResult.games()) {
                games += i + ". " + game.gameName() + ": WHITE=" + game.whiteUsername()
                        + ", BLACK=" + game.blackUsername() + "\n";
                i++;
            }
            return games;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String playGame() throws RuntimeException {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please pick a number game to join");
            PreLoginClient.printPrompt();
            String gameNum = scanner.nextLine();
            int gameID = getGameID(gameNum);

            System.out.println("Please pick black or white");
            PreLoginClient.printPrompt();
            String color = scanner.nextLine();
            switch (color.toLowerCase()) {
                case "black" -> playerColor = ChessGame.TeamColor.BLACK;
                case "white" -> playerColor = ChessGame.TeamColor.WHITE;
                default -> throw new RuntimeException("ERROR: Invalid team selected");
            }

            PreLoginClient.server.joinGame(PreLoginClient.authToken, playerColor, gameID);
            return "Joined game successfully";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String observeGame() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please pick a number game to observe");
            PreLoginClient.printPrompt();
            String gameNum = scanner.nextLine();
            int gameID = getGameID(gameNum);

            // additional functionality will be added in phase 6
            return "Now observing game";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String help() {
        return """
                 - logout
                 - createGame
                 - listGames
                 - playGame
                 - observeGame
                 - help
                """;
    }

    private boolean isEnteringGame(String message) {
        return Objects.equals(message, "Joined game successfully") ||
                Objects.equals(message, "Now observing game");
    }

    private static int getGameID(String gameNum) throws RuntimeException {
        ListGamesResult listGamesResult = PreLoginClient.server.listGames(PreLoginClient.authToken);
        int gameID = 0;
        int i = 1;
        for (Game game : listGamesResult.games()) {
            if (Objects.equals(gameNum, String.valueOf(i))) {
                gameID = game.gameID();
            }
            i++;
        }
        if (gameID == 0) {
            throw new RuntimeException("ERROR: Invalid game number");
        }
        return gameID;
    }
}
