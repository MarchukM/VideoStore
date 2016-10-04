package com.videostore.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 22.09.2016.
 */
public class Cart {
    private ArrayList<Film> films;
    private BigDecimal totalCost;

    public Cart(){
        totalCost = new BigDecimal(0);
        films = new ArrayList<>();
    }

    public void addFilm(Film film){
        films.add(film);
    }

    public ArrayList<Film> getFilms(){
        return films;
    }

    public Film getById(int id) {
        Film film = null;
        for (Film f : films) {
            if(f.getFilmId() == id){
                film = f;
            }
        }
        return film;
    }

    public int getNumOfFilms(){
        return films.size();
    }

    public boolean contains(Film film){
        return films.contains(film);
    }

    public boolean isEmpty(){
        return films.isEmpty();
    }

    public boolean remove(Film film){
        return films.remove(film);
    }

    public Double getTotalCost(){
        totalCost = new BigDecimal(0);
        for(Film f : films){
            totalCost = totalCost.add(new BigDecimal(f.getPrice())
                    .setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        return totalCost.doubleValue();
    }
}
