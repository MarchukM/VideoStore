package com.videostore.mysql;

import com.videostore.dao.DaoFactory;
import com.videostore.dao.FilmDao;
import com.videostore.dao.OrderDao;
import com.videostore.dao.UserDao;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;



public final class MySqlDaoFactory implements DaoFactory {

    private final BasicDataSource ds;

    public MySqlDaoFactory() {
        System.out.println("initialize BasicDataSource");
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/videostore2?autoReconnect=true&useSSL=false&characterEncoding=cp1251");
        ds.setUsername("root");
        ds.setPassword("root");
    }


    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public UserDao getUserDao(final Connection connection) {
        return new MySqlUserDao(connection);
    }

    @Override
    public FilmDao getFilmDao(final Connection connection) {
        return new MySqlFilmDao(connection);
    }

    @Override
    public OrderDao getOrderDao(final Connection connection) {
    return new MySqlOrderDao(connection);
}
}