package dataaccess;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    @Override
    public User getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM user WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String password = rs.getNString("password");
                String email = rs.getNString("email");

                return new User(username, password, email);
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not establish a connection to the database");
        }
    }

    @Override
    public void addUser(User user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String username = user.username();
        String password = user.password();
        String email = user.email();

        DatabaseManager.executeUpdate(statement, username, password, email);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String statement = "TRUNCATE TABLE user";
        DatabaseManager.executeUpdate(statement);
    }
}
