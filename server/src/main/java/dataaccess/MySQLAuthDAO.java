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
        int id = DatabaseManager.executeUpdate(statement, String.valueOf(authToken.authToken()), user.username());

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
        String statement = "SELECT id FROM authToken WHERE authToken=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not establish a connection to the database: " + e.getMessage());
        }
    }

    @Override
    public User getUser(String authToken) throws DataAccessException {
        String statement = "SELECT authToken, username FROM authToken where authToken=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");

                statement = "SELECT * FROM user where username=?";
                ps = conn.prepareStatement(statement);
                ps.setString(1, username);

                rs = ps.executeQuery();

                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");

                    return new User(username, password, email);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not establish a connection to the database: " + e.getMessage());
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
