package com.videostore.mysql;

import com.videostore.dao.FilmDao;
import com.videostore.domain.Film;
import com.videostore.exceptions.PersistException;
import com.videostore.utilities.TextUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is being used for access to database and operations
 * connected to table "Film"
 *
 * @version 1.0
 * @autor Maxim Marchuk
 */
public final class MySqlFilmDao implements FilmDao {
    /**
     * log4j instance
     */
    private static final Logger log = Logger.getLogger(com.videostore.mysql.MySqlFilmDao.class);

    /**
     *  A connection (session) with a specific
     * database.
     *
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
    public MySqlFilmDao(Connection connection) {
        this.connection = connection;
        this.noOfRecords = 0;
    }

    /**
     * Method for creating a new record in the film table
     *
     * @param film - an object representing film entity
     * @return true - if all goes well
     * @throws SQLException
     */
    @Override
    public boolean create(final Film film) throws SQLException {
        PreparedStatement stm = null;
        Integer nextFilmId = null;
        ArrayList<Integer> genreIds = new ArrayList<>();

        String insertFilm = "INSERT INTO videostore2.film (title, " +
                "producer, year, run_time, country, cover, description, price)" +
                "values (?,?,?,?,?,?,?,?);";
        String findOutFilmId = "SELECT auto_increment FROM " +
                "information_schema.tables where table_name='film';";
        String findOutGenreId = "SELECT genre_id FROM genre WHERE genre=?";
        String insertGenres = "INSERT INTO videostore2.film_genre " +
                "(film_id, genre_id) values (?, ?)";

        try {
            //check film_id of a new film to be added
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                nextFilmId = result.getInt(1);
            }
            if (nextFilmId == null) {
                throw new PersistException("Unable to get next film_id");
            }

            //check genre_if for all genres
            ArrayList<String> genres = film.getGenres();
            for (String s : genres) {
                stm = connection.prepareStatement(findOutGenreId);
                stm.setString(1, s);
                result = stm.executeQuery();
                if (result.next()) {
                    genreIds.add(result.getInt(1));
                }
                if (genreIds.isEmpty()) {
                    throw new PersistException("Something goes wrong when checking genre_id");
                }
            }
        } catch (SQLException e) {
            log.error("Creating problem", e);
        } catch (PersistException e) {
            log.error("Creating problem", e);
        }

        try {
            //Switching off autocommit for transaction
            connection.setAutoCommit(false);

            stm = connection.prepareStatement(insertFilm);
            stm.setString(1, film.getTitle());
            stm.setString(2, film.getProducer());
            stm.setInt(3, film.getYear());
            stm.setTime(4, film.getRunTime());
            stm.setString(5, film.getCountry());
            stm.setString(6, film.getCover());
            stm.setString(7, film.getDescription());
            stm.setDouble(8, film.getPrice().doubleValue());
            stm.executeUpdate();

            for (Integer genreId : genreIds) {
                stm = connection.prepareStatement(insertGenres);
                stm.setInt(1, nextFilmId);
                stm.setInt(2, genreId);
                stm.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            log.error("create", e);
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
        return true;
    }

    /**
     * Method for getting a record from database for film entity by key
     *
     * @param key - film_id is the key in this case
     * @return Film - if all goes well else null
     * @throws SQLException
     * @see Film
     */
    @Override
    public Film read(final int key) throws SQLException {
        Film film = null;
        PreparedStatement stm = null;
        String sql = "select film_id, `title`, `producer`, `year`, `run_time`,`description`, `country`, `cover`, `price`," +
                " GROUP_CONCAT(`genre`) as `genre` from videostore2.film LEFT OUTER JOIN film_genre" +
                " using(film_id) LEFT OUTER JOIN genre using(genre_id) WHERE film_id=?;";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, key);
            ResultSet rs = stm.executeQuery();

            ArrayList<Film> filmInfo = parseResultSet(rs);
            if (filmInfo.isEmpty()) {
                throw new PersistException("Film not found");
            }
            film = filmInfo.iterator().next();

        } catch (SQLException e) {
            log.error("Read problem", e);
        } catch (PersistException e) {
            log.error("Read problem", e);
        } finally {

            if (stm != null) {
                stm.close();
            }
        }
        return film;
    }


    @Override
    public void update(final Film film) {
    }

    @Override
    public void delete(final Film film) {
    }

    /**
     * Method standing for getting specific part of film records from database
     *
     * @param offset      - starting from zero
     * @param noOfRecords - quantity of records
     * @return ArrayList container for Film class with specific records
     * @throws SQLException
     */
    @Override
    public ArrayList<Film> getPart(final int offset, final int noOfRecords) throws SQLException {
        PreparedStatement statement = null;
        ArrayList<Film> films = new ArrayList<>();

        String sql = "SELECT SQL_CALC_FOUND_ROWS `film_id`, `title`, `producer`, `year`, `run_time`,`description`, `country`, `cover`, `price`," +
                " GROUP_CONCAT(`genre`) AS `genre` FROM videostore2.film LEFT OUTER JOIN film_genre" +
                " USING(film_id) LEFT OUTER JOIN genre USING(genre_id) GROUP BY film_id LIMIT ?,?;";

        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2, noOfRecords);
            ResultSet rs = statement.executeQuery();
            films = parseResultSet(rs);

            rs = statement.executeQuery("SELECT FOUND_ROWS();");
            if (rs.next()) {
                this.noOfRecords = rs.getInt(1);
            } else {
                throw new PersistException("Cannot get entire number of records");
            }
        } catch (SQLException e) {
            log.error("getting part of film records problem", e);
        } catch (PersistException e) {
            log.error("getting part of film records problem", e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return films;
    }


    /**
     * Method for checking entire number of film records
     *
     * @return noOfRecords - entire number of records
     */
    public int getNoOfRecords() {
        return noOfRecords;
    }


    /**
     * Method for ResultSet parsing
     *
     * @param rs - ResultSet for parsing
     * @return - ArrayList container for film objects with parsing result
     * @throws SQLException
     */
    private ArrayList<Film> parseResultSet(final ResultSet rs) throws SQLException, PersistException {
        ArrayList<Film> list = new ArrayList<>();

        while (rs.next()) {
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

            ArrayList<String> g = new ArrayList<>();
            String genres = rs.getString("genre");

            //parse genre string for split genres
            if (!TextUtil.isEmpty(genres)) {
                String[] genresArr = genres.split(",");
                for (String s : genresArr) {
                    g.add(s);
                }
            }

            film.setGenres(g);
            list.add(film);
        }
        if (list.isEmpty()) {
            throw new PersistException("List of genres is empty");
        }

        return list;
    }
}
