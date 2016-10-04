package com.videostore.domain;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by max on 20.09.2016.
 */
public class Film {
    private int filmId;
    private String title;
    private Integer year;
    private Time runTime;
    private String country;
    private String cover;
    private String producer;
    private String description;
    private BigDecimal price;
    private ArrayList<String> genres;

    public Film(){
        this.price = new BigDecimal(0);
        this.genres = new ArrayList<>();
    }

    public Double getPrice() {
        return price.doubleValue();
    }

    public void setPrice(Double price) {
        this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Time getRunTime() {
        return runTime;
    }

    public void setRunTime(Time runtime) {
        this.runTime = runtime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void printDetails() {
        System.out.println(getFilmId());
        System.out.println(getTitle());
        System.out.println(getProducer());
        System.out.println(getPrice());
        System.out.println(getCover());
        System.out.println(getCountry());
        System.out.println(getRunTime());

        for(String s : genres){
            System.out.println(s);
        }
    }
}
