package dataaccess;

import model.AuthToken;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO {
    @Override
    public AuthToken addAuthToken(User user) throws DataAccessException {
        var statement = "INSERT INTO authToken (authToken, username) VALUES (?, ?)";
        AuthToken authToken = createAuthToken();
        int id = DatabaseManager.executeUpdate(statement, authToken, user.username());

        if (id != 0) {
            return authToken;
        }
        return null;
    }

    @Override
    public void clearAuthTokens() throws DataAccessException {
        String statement = "TRUNCATE TABLE authToken";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public boolean containsAuthToken(String authToken) throws DataAccessException {
        String statement = "SELECT authToken FROM authToken WHERE authToken=?";
        int id = DatabaseManager.executeUpdate(statement, authToken);
        return id != 0;
    }

    @Override
    public User getUser(String authToken) throws DataAccessException {
        String statement = "SELECT authToken, username FROM authToken where authToken=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getNString("username");

                statement = "SELECT * FROM user where username=?";
                ps = conn.prepareStatement(statement);
                rs = ps.executeQuery();

                if (rs.next()) {
                    String password = rs.getNString("password");
                    String email = rs.getNString("email");

                    return new User(username, password, email);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not establish a connection to the database");
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String statement = "DELETE FROM authToken WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public AuthToken createAuthToken() {
        return AuthDAO.super.createAuthToken();
    }
}
