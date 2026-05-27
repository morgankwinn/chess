package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import request.RegisterRequest;
import result.RegisterResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.RegisterService;

public class RegisterHandler {

    public static void handleRegister(Context ctx, UserDAO userDao, AuthDAO authDao)
            throws AlreadyTakenException, BadRequestException, DataAccessException {
        RegisterRequest request = (RegisterRequest) new Gson().fromJson(ctx.body(), RegisterRequest.class);

        RegisterService service = new RegisterService();
        RegisterResult result = service.register(request, userDao, authDao);

        ctx.result(new Gson().toJson(result));
    }
}
