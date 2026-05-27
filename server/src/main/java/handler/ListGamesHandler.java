package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import request.ListGamesRequest;
import result.ListGamesResult;
import service.BadRequestException;
import service.ListGamesService;
import service.UnauthorizedException;

public class ListGamesHandler {
    public static void handleListGames(Context ctx, GameDAO gameDao, AuthDAO authDao)
            throws UnauthorizedException, BadRequestException, DataAccessException {
        ListGamesRequest request = new ListGamesRequest(ctx.header("authorization"));

        ListGamesService service = new ListGamesService();
        ListGamesResult result = service.listGames(request, authDao, gameDao);

        ctx.result(new Gson().toJson(result));
    }
}
