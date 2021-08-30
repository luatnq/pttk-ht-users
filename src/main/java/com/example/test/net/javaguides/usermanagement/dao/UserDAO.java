package com.example.test.net.javaguides.usermanagement.dao;

import com.example.test.net.javaguides.usermanagement.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static Connection connection = null;
    public static String dbName = "demo";
    public static String username = "root";
    public static String password = "luatbeo";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
            + " (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    public UserDAO() {
        if(connection == null){
            String dbUrl = "jdbc:mysql://localhost:3306/"+ dbName + "?autoReconnect=true&useSSL=false";
            String dbClass = "com.mysql.cj.jdbc.Driver";
            try {
                Class.forName(dbClass);
//                System.out.println(dbClass);
                connection = DriverManager.getConnection (dbUrl, username, password);
                System.out.println(connection);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> selectAllUsers() {
        // using try-with-resources to avoid closing resources (boiler plate code)
        List<User> users = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
        }
        return users;
    }

    public User selectUser(int id) {
        User user = null;
        // Step 1: Establishing a Connection
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
        }
        return user;
    }

    public boolean updateUser(User book) {
        boolean rowUpdated;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getEmail());
            statement.setString(3, book.getCountry());
            statement.setInt(4, book.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        catch (Exception e) {
            // TODO: handle exception
            rowUpdated = false;
        }
        return rowUpdated;
    }

    public boolean deleteUser(int id) {
        boolean rowDeleted;
        try( PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        catch (Exception e) {
            // TODO: handle exception
            rowDeleted = false;
        }
        return rowDeleted;
    }

    public void insertUser(User newUser) {
        System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, newUser.getName());
            preparedStatement.setString(2, newUser.getEmail());
            preparedStatement.setString(3, newUser.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
