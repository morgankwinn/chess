package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.JoinGameService;
import service.UnauthorizedException;

public class JoinGameHandler {
    public static void handleJoinGame(Context ctx, GameDAO gameDao, AuthDAO authDao)
            throws UnauthorizedException, AlreadyTakenException, BadRequestException {
        JoinGameRequest request = (JoinGameRequest) new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        request.setAuthToken(ctx.header("authorization"));

        JoinGameService service = new JoinGameService();
        service.joinGame(request, gameDao, authDao);
    }
}
