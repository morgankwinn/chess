package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import request.LoginRequest;
import result.LoginResult;
import service.BadRequestException;
import service.LoginService;
import service.UnauthorizedException;
import service.UserDoesNotExistException;

public class LoginHandler {
    public static void handleLogin(Context ctx, UserDAO userDao, AuthDAO authDao)
            throws UserDoesNotExistException, BadRequestException, UnauthorizedException, DataAccessException {
        LoginRequest request = (LoginRequest) new Gson().fromJson(ctx.body(), LoginRequest.class);

        LoginService service = new LoginService();
        LoginResult result = service.login(request, userDao, authDao);

        ctx.result(new Gson().toJson(result));
    }
}
