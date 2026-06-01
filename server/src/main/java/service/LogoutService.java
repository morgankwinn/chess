package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.request.LogoutRequest;

public class LogoutService {
    public void logout(LogoutRequest request, AuthDAO authDao)
            throws UnauthorizedException, BadRequestException, DataAccessException {
        try {
            String authToken = request.authToken();

            if (authToken == null) {
                throw new BadRequestException("ERROR: no Auth Token");
            }
            if (!authDao.containsAuthToken(authToken)) {
                throw new UnauthorizedException("ERROR: Auth Token invalid");
            }

            authDao.deleteAuthToken(authToken);
        } catch (NullPointerException e) {
            throw new UnauthorizedException("ERROR: no Auth Token");
        }
    }
}
