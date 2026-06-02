package ui;

import java.util.Objects;
import java.util.Scanner;

public class LoginClient {

    public void run() {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("Logout successful")) {
            PreLoginClient.printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

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

    //    Allows the user to input a name for the new game. Calls the server create API to create the game.
//    This does not join the player to the created game; it only creates the new game in the server.
    private String createGame() {
    }

    //    Lists all the games that currently exist on the server.
//    Calls the server list API to get all the game data, and displays the games in a numbered list,
//    including the game name and players (not observers) in the game.
//    The numbering for the list should be independent of the game IDs and should start at 1.
    private String listGames() {
    }

    //    Allows the user to specify which game they want to join and what color they want to play.
//    They should be able to enter the number of the desired game.
//    Your client will need to keep track of which number corresponds to which game from the last time it listed
//    the games.
//    Calls the server join API to join the user to the game.
    private String playGame() {
    }

    //    Allows the user to specify which game they want to observe.
//    They should be able to enter the number of the desired game.
//    Your client will need to keep track of which number corresponds to which game from the last time it listed
//    the games.
    private String observeGame() {
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
        return Objects.equals(message, "") ||
                Objects.equals(message, "");
    }
}
