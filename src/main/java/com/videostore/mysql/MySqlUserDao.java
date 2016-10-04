package com.videostore.mysql;

import com.videostore.dao.UserDao;
import com.videostore.domain.User;
import com.videostore.exceptions.PersistException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is being used for access to database and operations
 * connected to table "User"
 *
 * @version 1.0
 * @autor Maxim Marchuk
 */

public final class MySqlUserDao implements UserDao {

    /**
     * log4j instance
     */
    private static final Logger log = Logger.getLogger(com.videostore.mysql.MySqlUserDao.class);

    /**
     * A connection (session) with a specific
     * database.
     *
     * @see Connection
     */
    private final Connection connection;

    /**
     * Class constructor creates a new object with specific connection
     *
     * @param connection - An object providing connection with database
     */
    public MySqlUserDao(Connection connection) {
        this.connection = connection;
    }


    /**
     * Method for creating a new record in the user table
     *
     * @param user - an object representing user entity
     * @return true - if all goes well
     * @throws SQLException
     * @see User
     */
    public User create(final User user) throws SQLException {
        PreparedStatement statement = null;

        String insertDataIntoUser = "INSERT INTO videostore2.user (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?)";
        String getNewUserRecord = "SELECT *FROM videostore2.user WHERE user_id = last_insert_id();";

        try {
            statement = connection.prepareStatement(insertDataIntoUser);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());

            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("Got back more than one record: " + count);
            }

        } catch (SQLException e) {
            log.error("Creating new user problem", e);
        } catch (PersistException e) {
            log.error(e);
        }

        User newUser = null;

        // Получаем только что вставленную запись
        try {
            statement = connection.prepareStatement(getNewUserRecord);
            ResultSet rs = statement.executeQuery();
            List<User> list = new ArrayList<>();
            list = parseResultSet(rs);

            if (list.size() > 1) {
                throw new PersistException("Got more than one record" + list.size());
            }
            newUser = list.iterator().next();

        } catch (SQLException e) {
            log.error("Creating new user problem", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return newUser;
    }


    /**
     * Method for ResultSet parsing
     *
     * @param rs - ResultSet for parsing
     * @return - ArrayList container for User objects with parsing result
     * @throws SQLException
     */
    protected ArrayList<User> parseResultSet(final ResultSet rs) throws SQLException, PersistException {
        ArrayList<User> list = new ArrayList<>();
        try {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setUserName(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setRegDate(rs.getTimestamp("reg_date"));
                u.setAdmin(rs.getBoolean("admin"));
                list.add(u);
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
        }

        if (list.isEmpty()) {
            throw new PersistException("Got an empty ResultSet");
        }

        return list;
    }


    /**
     * Method for getting user record by username and password
     *
     * @param username
     * @param password
     * @return User object
     * @throws SQLException
     * @see User
     */
    @Override
    public User getByCredentials(final String username, final String password) throws SQLException {
        PreparedStatement statement = null;
        User user = null;

        String sql = "SELECT * FROM videostore2.user WHERE username=? AND password=?";

        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            List<User> list = parseResultSet(rs);

            if (list.size() > 1) {
                throw new PersistException("Got more than one record");
            }

            user = list.iterator().next();
        } catch (SQLException e) {
            log.error("Something wrong with get by credentials", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {

            if (statement != null) {
                statement.close();
            }
        }

        return user;
    }

    /**
     * Method for getting user's record by key, where key is user_id
     *
     * @param key - user_id
     * @return User object
     * @throws SQLException
     * @see User
     */
    @Override
    public User read(final int key) throws SQLException {
        User user = null;
        PreparedStatement statement = null;

        String selectFromUser = "SELECT * FROM videostore2.user WHERE user_id = ?;";
        try {
            statement = connection.prepareStatement(selectFromUser);
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            List<User> list = parseResultSet(rs);

            if (list.size() > 1) {
                throw new PersistException("Got more than one record: " + list.size());
            }
            user = list.iterator().next();

        } catch (SQLException e) {
            log.error("Reading problem", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return user;
    }

    /**
     * Method for updating user's record
     *
     * @param user
     * @throws SQLException
     */
    @Override
    public void update(final User user) throws SQLException {
        PreparedStatement statement = null;
        String updateUserData = "UPDATE videostore2.user SET username=?, password=?, first_name=?, last_name=?, ema_il=? WHERE user_id=?";
        try {
            statement = connection.prepareStatement(updateUserData);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setInt(6, user.getId());

            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On updating modify more than one record" + count);
            }
        } catch (SQLException e) {
            log.error("Updating problem", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

    }

    /**
     * Method for deleting user's data from database
     * @param user - user who's record will be deleted
     * @throws SQLException
     */
    @Override
    public void delete(final User user) throws SQLException {
        PreparedStatement statement = null;
        String sql = "DELETE FROM videostore2.user WHERE username=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUserName());
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            log.error("Deleting problem", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {
            if(statement != null){
                statement.close();
            }
        }
    }


    /**
     * Method for getting all user's records from db
     * @return List object containing all entries of users
     * @throws SQLException
     */
    @Override
    public List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM videostore2.user;";
        PreparedStatement stm = connection.prepareStatement(sql);
        ResultSet rs = stm.executeQuery();
        List<User> list = null;
        try {
            list = parseResultSet(rs);
        } catch (PersistException e) {
            log.error(e);
        }

        return list;
    }
}