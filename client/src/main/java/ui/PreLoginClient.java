package ui;

import model.result.LoginResult;
import model.result.RegisterResult;
import server.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class PreLoginClient {
    public static ServerFacade server;
    public static String authToken;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess. Register to start");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if (isLoggedIn(result)) {
                    LoginClient loginClient = new LoginClient();
                    loginClient.run();
                }
            } catch (Exception e) {
                notify(e.getMessage());
            }
        }
        System.out.println();
    }

    public static void printPrompt() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public static void notify(String notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification);
    }

    private static String eval(String input) throws RuntimeException {
        try {
            return switch (input.toLowerCase()) {
                case "register" -> register();
                case "login" -> login();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String register() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please provide your username");
            printPrompt();
            String username = scanner.nextLine();

            System.out.println("Please provide your password");
            printPrompt();
            String password = scanner.nextLine();

            System.out.println("Please provide your email");
            printPrompt();
            String email = scanner.nextLine();

            RegisterResult registerResult = server.register(username, password, email);
            authToken = registerResult.authToken();
            return "Register successful. You are now logged in";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String login() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please provide your username");
            printPrompt();
            String username = scanner.nextLine();

            System.out.println("Please provide your password");
            printPrompt();
            String password = scanner.nextLine();

            LoginResult loginResult = server.login(username, password);
            authToken = loginResult.authToken();
            return "Login successful";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String help() {
        return """
                 - register
                 - login
                 - help
                 - quit
                """;
    }

    private static boolean isLoggedIn(String message) {
        return Objects.equals(message, "Register successful. You are now logged in") ||
                Objects.equals(message, "Login successful");
    }
}
