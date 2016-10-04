package com.videostore.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.videostore.dao.OrderDao;
import com.videostore.domain.Film;
import com.videostore.domain.Order;
import com.videostore.exceptions.PersistException;
import com.videostore.utilities.TextUtil;
import org.apache.log4j.Logger;

/**
 * This class is being used for access to database and operations
 * connected to table "Order"
 *
 * @version 1.0
 * @autor Maxim Marchuk
 */
public final class MySqlOrderDao implements OrderDao {

    /**
     * log4j instance
     */
    private static final Logger log = Logger.getLogger(com.videostore.mysql.MySqlOrderDao.class);

    /**
     * A connection with a specific
     * database.
     * @see Connection
     */
    private final Connection connection;

    /**
     * Is being used for keeping entire number of records
     */
    private int noOfRecords;

    /**
     * Class constructor creates a new object with specific connection
     *
     * @param connection - An object providing connection with database
     */
    public MySqlOrderDao(Connection connection) {
        this.connection = connection;
        this.noOfRecords = 0;
    }


    /**
     * Method standing for getting specific part of order records from database
     *
     * @param offset      - starting from zero
     * @param noOfRecords - quantity of records
     * @return ArrayList container for Order class with specific records
     * @throws SQLException
     */
    @Override
    public ArrayList<Order> getPart(final int offset, final int noOfRecords) throws SQLException {
        PreparedStatement statementForOrder = null;
        ArrayList<Order> ordersList = new ArrayList<>();

        String getFromOrderTable = " select SQL_CALC_FOUND_ROWS order_id, user_id, name, address, telephone, date," +
                " GROUP_CONCAT(`film_id`) as `film_id` from videostore2.order LEFT OUTER JOIN" +
                " order_film using(order_id) group by order_id LIMIT ?, ? ";

        try {
            statementForOrder = connection.prepareStatement(getFromOrderTable);
            PreparedStatement tempStm = connection.prepareCall("SELECT FOUND_ROWS();");

            statementForOrder.setInt(1, offset);
            statementForOrder.setInt(2, noOfRecords);

            ResultSet resultSet = statementForOrder.executeQuery();

            //временный резалт сет нужен для выяснения общего кол-ва записей
            ResultSet tempResultSet = tempStm.executeQuery();
            if (tempResultSet.next()) {
                this.noOfRecords = tempResultSet.getInt(1);
            }

            ordersList = parseOrderResultSet(resultSet);

        } catch (SQLException e) {
            log.error("Getting part problem", e);
        } catch (PersistException e) {
            log.error(e);
        } finally {
            if (statementForOrder != null) {
                statementForOrder.close();
            }
        }

        return ordersList;
    }

    /**
     * Method for getting order's record by key, where key is user_id
     *
     * @param id - order_id
     * @return User object
     * @throws SQLException
     * @see Order
     */
    @Override
    public Order getById(final int id) throws SQLException {
        Order order = null;
        PreparedStatement statement = null;

        String getFromOrderTable = " select SQL_CALC_FOUND_ROWS order_id, user_id, name, address, telephone, date," +
                " GROUP_CONCAT(`film_id`) as `film_id` from videostore2.order LEFT OUTER JOIN" +
                " order_film using(order_id) WHERE order_id=?";

        try {
            statement = connection.prepareStatement(getFromOrderTable);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            ArrayList<Order> list = parseOrderResultSet(resultSet);

            if(list.size() > 1){
                throw new PersistException("Got more than one record: " + list.size());
            }
            order = list.iterator().next();

        } catch (SQLException e) {
            log.error("Get by id problem", e);
        } catch (PersistException e) {
            log.error("Get by id problem", e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return order;
    }

    /**
     * Method for checking entire records of orders
     *
     * @return noOfRecords - entire number of records
     */
    @Override
    public int getNoOfRecords() {
        return noOfRecords;
    }


    /**
     * Method for parsing Order's ResultSet
     * @param resultSet - ResultSet object for parsing
     * @return - ArrayList container of Order objects
     * @throws SQLException
     * @see Order
     */
    private ArrayList<Order> parseOrderResultSet(final ResultSet resultSet) throws SQLException, PersistException {
        ArrayList<Order> list = new ArrayList<>();

        String getFilmById = "select film_id, `title`, `producer`, `year`, `run_time`,`description`, `country`, `cover`, `price`," +
                " GROUP_CONCAT(`genre`) as `genre` from videostore2.film LEFT OUTER JOIN film_genre" +
                " using(film_id) LEFT OUTER JOIN genre using(genre_id) WHERE film_id=?;";

        while (resultSet.next()) {
            Order order = new Order();
            order.setOrderId(resultSet.getInt("order_id"));
            order.setUserId(resultSet.getInt("user_id"));
            order.setName(resultSet.getString("name"));
            order.setAddress(resultSet.getString("address"));
            order.setTelephone(resultSet.getString("telephone"));
            order.setOrderDate(resultSet.getTimestamp("date"));

            //getting list of ordered film id's
            //and convert string to int
            String strFilmId = resultSet.getString("film_id");

            if(TextUtil.isEmpty(strFilmId)){
                throw new PersistException("Cannot get list of film_id");
            }

            String[] strFilmIdArray = strFilmId.split(",");
            Integer[] intFilmIdArray = new Integer[strFilmIdArray.length];

            for (int i = 0; i < strFilmIdArray.length; i++) {
                intFilmIdArray[i] = Integer.valueOf(strFilmIdArray[i]);
            }

            //Go through all film_id's and get films from db and add them to the order
            for (Integer id : intFilmIdArray) {
                PreparedStatement stm = connection.prepareStatement(getFilmById);
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();

                if (rs.next()) {
                    Film film = parseFilmResultSet(rs);
                    order.addFilm(film);
                }
            }
            list.add(order);
        }
        return list;
    }


    /**
     * Method for ResultSet entries parsing from film table, needed for forming order
     * @param rs - ResultSet object for parsing
     * @return Film object
     * @throws SQLException
     * @see Film
     */
    private Film parseFilmResultSet(final ResultSet rs) throws SQLException, PersistException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setYear(rs.getInt("year"));
        film.setRunTime(rs.getTime("run_time"));
        film.setCountry(rs.getString("country"));
        film.setCover(rs.getString("cover"));
        film.setProducer(rs.getString("producer"));
        film.setDescription(rs.getString("description"));
        film.setPrice(rs.getDouble("price"));

        ArrayList<String> genres = new ArrayList<>();
        String genresStr = rs.getString("genre");

        //Parse "genres" string to separate genres
        if (!TextUtil.isEmpty(genresStr)) {
            String[] genresArr = genresStr.split(",");
            for (String s : genresArr) {
                genres.add(s);
            }
        }

        if(genres.isEmpty()){
            throw new PersistException("Cannot get genres of film");
        }

        film.setGenres(genres);

        return film;
    }


    /**
     * Method for creating a new order record
     * @param order - Order object prepared for creating of a new record
     * @throws SQLException
     * @see Order
     */
    @Override
    public void create(final Order order) throws SQLException {
        PreparedStatement stm = null;
        Integer nextId = null;

        String insertDetails = "INSERT INTO videostore2.order (user_id, name, address, telephone) VALUES (?,?,?,?);";
        String findOutId = "SELECT auto_increment FROM information_schema.tables where table_name='order';";
        String insertFilms = "INSERT INTO videostore2.order_film (order_id, film_id) VALUES (?, ?);";

        try {
            stm = connection.prepareStatement(findOutId);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                nextId = result.getInt(1);
            }
            if (nextId == null) {
                throw new NoSuchElementException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            connection.setAutoCommit(false);

            stm = connection.prepareStatement(insertDetails);
            stm.setInt(1, order.getUserId());
            stm.setString(2, order.getName());
            stm.setString(3, order.getAddress());
            stm.setString(4, order.getTelephone());
            stm.executeUpdate();

            ArrayList<Film> films = order.getOrderedFilms();
            for (Film f : films) {
                stm = connection.prepareStatement(insertFilms);
                stm.setInt(1, nextId);
                stm.setInt(2, f.getFilmId());
                stm.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            log.error("Creating problem", e);
        } finally {

            if (stm != null) {
                stm.close();
            }
        }
    }
}
