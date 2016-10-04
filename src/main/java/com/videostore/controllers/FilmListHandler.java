package com.videostore.controllers;

import com.videostore.utilities.TextUtil;
import com.videostore.dao.DaoFactory;
import com.videostore.dao.FilmDao;
import com.videostore.domain.Film;
import com.videostore.mysql.MySqlDaoFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FilmListHandler extends HttpServlet {
    public static final Logger log = Logger
            .getLogger(FilmListHandler.class);
    public static final int RECORDS_PER_PAGE = 4;
    public static final String UPLOAD_PATH = "D:\\images\\";


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        MySqlDaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                .getAttribute("daoFactory");

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        try (Connection con = daoFactory.getConnection()) {
            FilmDao dao = daoFactory.getFilmDao(con);
            List<Film> list = dao.getPart((page - 1) * RECORDS_PER_PAGE, RECORDS_PER_PAGE);
            int noOfRecords = dao.getNoOfRecords();
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / RECORDS_PER_PAGE);

            request.setAttribute("filmList", list);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            RequestDispatcher view = request.getRequestDispatcher("/protected/main.jsp");
            view.forward(request, response);

        } catch (SQLException e) {
            log.error(e);
        }
    }


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> messages = new HashMap<>();
        Film film = parseMultipartData(request, response, messages);

        DaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                .getAttribute("daoFactory");

        for (Map.Entry<String, String> entry : messages.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " : " + value);
        }
        if (messages.isEmpty()) {
            try (Connection connection = daoFactory.getConnection()) {
                FilmDao dao = daoFactory.getFilmDao(connection);
                dao.create(film);
            } catch (SQLException e) {
                log.error(e);
            }
        } else {
            request.setAttribute("messages", messages);
            RequestDispatcher view = request
                    .getRequestDispatcher("/protected/main.jsp?mode=admin&action=addFilm");
            view.forward(request, response);
        }
    }

    private Film parseMultipartData(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Map<String, String> messages) {

        String title = null;
        String producer = null;
        String country = null;
        String description = null;
        String cover = null;
        String yearStr = null;
        String priceStr = null;
        String runTimeStr = null;
        ArrayList<String> genres = new ArrayList<>();

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldNameISO = item.getFieldName();
                    byte ptext1[] = fieldNameISO.getBytes(ISO_8859_1);
                    String fieldName = new String(ptext1, UTF_8);

                    String fieldValueISO = item.getString();
                    byte ptext2[] = fieldValueISO.getBytes(ISO_8859_1);
                    String fieldValue = new String(ptext2, UTF_8);

                    switch (fieldName) {
                        case "title":
                            title = fieldValue;
                            break;

                        case "producer":
                            producer = fieldValue;
                            break;

                        case "year":
                            yearStr = fieldValue;
                            break;

                        case "country":
                            country = fieldValue;
                            break;

                        case "description":
                            description = fieldValue;
                            break;

                        case "runtime":
                            runTimeStr = fieldValue;
                            break;

                        case "price":
                            priceStr = fieldValue;
                            break;
                        case "genre":
                            genres.add(fieldValue);
                            break;
                    }
                } else {
                    String fileName = item.getName();
                    if (fileName == "") {
                        continue;
                    }
                    byte ptext[] = fileName.getBytes();
                    fileName = new String(ptext, UTF_8);
                    cover = fileName;
                    File path = new File(UPLOAD_PATH);
                    File uploadedFile = new File(path + File.separator + fileName);
                    item.write(uploadedFile);
                }
            }
        } catch (FileUploadException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }

        if (TextUtil.isEmpty(title)) {
            messages.put("title", "Enter a title.");
        }

        if (TextUtil.isEmpty(producer)) {
            messages.put("producer", "Enter a producer.");
        }

        if (TextUtil.isEmpty(country)) {
            messages.put("country", "Enter a country.");
        }

        if (TextUtil.isEmpty(description)) {
            messages.put("description", "Enter a description.");
        }

        if (TextUtil.isEmpty(cover)) {
            messages.put("cover", "Choose a cover.");
        }

        Integer year = new Integer(0);
        if (!TextUtil.isEmpty(yearStr)) {
            year = Integer.valueOf(yearStr);
            Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1979 || year > currentYear) {
                messages.put("year", "Illegal year.");
            }
        } else {
            messages.put("year", "Enter an year.");
        }

        Double price = new Double(0);
        if (!TextUtil.isEmpty(priceStr)) {
            price = Double.valueOf(priceStr);
            if (price < 0) {
                messages.put("price", "Unexpected price.");
            }
        } else {
            messages.put("price", "Enter a price");

        }

        Time time = null;
        if (runTimeStr.isEmpty()) {
            messages.put("runTime", "Enter run time.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = 0;
            try {
                ms = sdf.parse(runTimeStr).getTime();
                time = new Time(ms);
            } catch (ParseException e) {
                messages.put("runTime", "Illegal run time");
                log.error(e);
            }
            System.out.println("Time: " + time);
        }

        if (genres.isEmpty()) {
            messages.put("genres", "Please select at least one genre.");
        }

        Film film = new Film();
        film.setGenres(genres);
        film.setTitle(title);
        film.setPrice(price);
        film.setProducer(producer);
        film.setCountry(country);
        film.setCover(cover);
        film.setRunTime(time);
        film.setYear(year);
        film.setDescription(description);

        return film;
    }

}