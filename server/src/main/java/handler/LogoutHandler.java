package handler;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.request.LogoutRequest;
import service.BadRequestException;
import service.LogoutService;
import service.UnauthorizedException;

public class LogoutHandler {
    public static void handleLogout(Context ctx, AuthDAO authDao)
            throws UnauthorizedException, BadRequestException, DataAccessException {
        LogoutRequest request = new LogoutRequest(ctx.header("authorization"));

        LogoutService service = new LogoutService();
        service.logout(request, authDao);
    }
}
