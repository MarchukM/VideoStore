package com.videostore.dao;

import java.sql.Connection;
import java.sql.SQLException;


public interface DaoFactory {

    public Connection getConnection() throws SQLException;

    public UserDao getUserDao(Connection connection);

    public FilmDao getFilmDao(Connection connection);

    public OrderDao getOrderDao(Connection connection);

}