package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import request.RegisterRequest;
import result.RegisterResult;
import service.AlreadyTakenException;
import service.RegisterService;

public class RegisterHandler {

    public static String handleRegister(Context ctx, UserDAO userDao, AuthDAO authDao) throws AlreadyTakenException {
        RegisterRequest request = (RegisterRequest) new Gson().fromJson(ctx.body(), RegisterRequest.class);

        RegisterService service = new RegisterService();
        RegisterResult result = service.register(request, userDao, authDao);

        return new Gson().toJson(result);
    }
}
