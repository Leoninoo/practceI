package crud;

import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDaoImpl implements UsersDao {
    private final Connection connection;

    private final String SQL_SELECT_BY_ID =
            "SELECT * FROM user WHERE id = ?";

    private final String SQL_SAVE_USER =
            "INSERT INTO user(login, password, email) VALUES (?, ?, ?)";

    private final String SQL_UPDATE_USER =
            "UPDATE user SET login = ?, email = ? WHERE (ID = ?)";

    private final String SQL_DELETE_USER =
            "DELETE FROM user WHERE id = ?";

    private final String SQL_SELECT_ALL =
            "SELECT * FROM user";

    public UsersDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User findById(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                User user = new User(login, password, email);
                user.setId(id);

                return user;
            }

            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(User u) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_USER);
            statement.setString(1, u.getLogin());
            statement.setString(2, u.getPassword());
            statement.setString(3, u.getEmail());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User u) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER);
            statement.setString(1, u.getLogin());
            statement.setString(2, u.getEmail());
            statement.setLong(3, u.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_USER);
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> products = new ArrayList<>();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                User user = new User(login, password, email);
                user.setId(id);

                products.add(user);
            }

            return products;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
