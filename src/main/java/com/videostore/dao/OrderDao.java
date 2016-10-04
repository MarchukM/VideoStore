package com.videostore.dao;

import com.videostore.domain.Order;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by max on 24.09.2016.
 */
public interface OrderDao {

    /**
     * Method for creating a new order record
     * @param order - Order object prepared for creating of a new record
     * @throws SQLException
     * @see Order
     */
    public void create(Order order) throws SQLException;

    /**
     * Method standing for getting specific part of order records from database
     *
     * @param offset      - starting from zero
     * @param noOfRecords - quantity of records
     * @return ArrayList container for Order class with specific records
     * @throws SQLException
     */
    public ArrayList<Order> getPart(int offset, int noOfRecords) throws SQLException;

    /**
     * Method for getting order's record by key, where key is user_id
     *
     * @param id - order_id
     * @return User object
     * @throws SQLException
     * @see Order
     */
    public Order getById(int id) throws SQLException;

    /**
     * Method for checking entire records of orders
     *
     * @return noOfRecords - entire number of records
     */
    public int getNoOfRecords();
}
