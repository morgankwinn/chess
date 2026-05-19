package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.BadRequestException;
import service.CreateGameService;
import service.UnauthorizedException;

public class CreateGameHandler {
    public static void handleCreateGame(Context ctx, GameDAO gameDao, AuthDAO authDao) throws UnauthorizedException, BadRequestException {
        CreateGameRequest request = (CreateGameRequest) new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        request.setAuthToken(ctx.header("authorization"));

        CreateGameService service = new CreateGameService();
        CreateGameResult result = service.createGame(request, gameDao, authDao);

        ctx.result(new Gson().toJson(result));
    }
}
