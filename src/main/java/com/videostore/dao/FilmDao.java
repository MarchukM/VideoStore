package com.videostore.dao;

import com.videostore.domain.Film;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by max on 20.09.2016.
 */
public interface FilmDao {

    /**
     * Method for creating a new record in the film table
     *
     * @param film - an object representing film entity
     * @return true - if all goes well
     * @throws SQLException
     */
    public boolean create(Film film) throws  SQLException;


    /**
     * Method for getting a record from database for film entity by key
     *
     * @param key - film_id is the key in this case
     * @return Film - if all goes well else null
     * @throws SQLException
     * @see Film
     */
    public Film read(int key) throws  SQLException;


    /**
     * Saves film object state in database
     * @param film - object for saving
     * @throws SQLException
     */
    public void update(Film film) throws SQLException;


    /**
     * Removes film entry from db
     * @param film - film that will be removed
     * @throws SQLException
     */
    public void delete(Film film) throws SQLException;

    /**
     * Method standing for getting specific part of film records from database
     *
     * @param offset      - starting from zero
     * @param noOfRecords - quantity of records
     * @return ArrayList container for Film class with specific records
     * @throws SQLException
     */
    public List<Film> getPart(int offset, int noOfRecords) throws SQLException;

    public int getNoOfRecords();

}
