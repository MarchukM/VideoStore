package com.videostore.dao;


import com.videostore.domain.User;

import java.sql.SQLException;
import java.util.List;


public interface UserDao {

    /**
     * Method for creating a new record in the user table
     *
     * @param user - an object representing user entity
     * @return true - if all goes well
     * @throws SQLException
     * @see User
     */
    public User create(User user) throws SQLException;

    /**
     * Method for getting user's record by key, where key is user_id
     *
     * @param key - user_id
     * @return User object
     * @throws SQLException
     * @see User
     */
    public User read(int key) throws SQLException;

    /**
     * Method for getting user record by username and password
     *
     * @param username
     * @param password
     * @return User object
     * @throws SQLException
     * @see User
     */
    public User getByCredentials(String username, String password) throws SQLException;

    /**
     * Method for updating user's record
     *
     * @param user
     * @throws SQLException
     */
    public void update(User user) throws SQLException;

    /**
     * Method for deleting user's data from database
     * @param user - user who's record will be deleted
     * @throws SQLException
     */
    public void delete(User user) throws SQLException;

    /**
     * Method for getting all user's records from db
     * @return List object containing all entries of users
     * @throws SQLException
     */
    public List<User> getAll() throws SQLException;
}