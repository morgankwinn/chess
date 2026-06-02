package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.request.*;
import model.result.CreateGameResult;
import model.result.ListGamesResult;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws RuntimeException {
        var request = buildRequest("DELETE", "/db", null, null);
        sendRequest(request);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws RuntimeException {
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        var request = buildRequest("POST", "/game", createGameRequest, authToken);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws RuntimeException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
        var request = buildRequest("PUT", "/game", joinGameRequest, authToken);
        sendRequest(request);
    }

    public ListGamesResult listGames(String authToken) throws RuntimeException {
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    public LoginResult login(String username, String password) throws RuntimeException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(String authToken) throws RuntimeException {
        var request = buildRequest("DELETE", "/session", null, authToken);
        sendRequest(request);
    }

    public RegisterResult register(String username, String password, String email) throws RuntimeException {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var request = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws RuntimeException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws RuntimeException {
        int status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new RuntimeException("ERROR: status code " + status + ": " + body);
            }

            throw new RuntimeException("ERROR: status code " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
