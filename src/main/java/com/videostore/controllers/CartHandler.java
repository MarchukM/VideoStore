package com.videostore.controllers;

import com.videostore.dao.FilmDao;
import com.videostore.domain.Cart;
import com.videostore.domain.Film;
import com.videostore.mysql.MySqlDaoFactory;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class CartHandler extends HttpServlet {
    private static final Logger log = Logger
            .getLogger(com.videostore.controllers.CartHandler.class);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String currentPage = request.getParameter("currentPage");
        Integer page = null;
        
        if (currentPage != null) {
            page = Integer.parseInt(request.getParameter("currentPage"));
        }

        Cart cart = (Cart) session.getAttribute("shoppingCart");
        MySqlDaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                .getAttribute("daoFactory");


        if (action.equalsIgnoreCase("add")) {
            try (Connection con = daoFactory.getConnection()) {
                FilmDao dao = daoFactory.getFilmDao(con);
                Integer id = Integer.valueOf(request.getParameter("id"));
                Film f = dao.read(id);

                if (!(cart.contains(f))) {
                    cart.addFilm(f);
                }

                RequestDispatcher view = request
                        .getRequestDispatcher("/FilmListHandler?page=" + page);
                view.forward(request, response);
            } catch (SQLException e) {
                log.error(e);
            }
        }

        if (action.equalsIgnoreCase("remove")) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            Film f = cart.getById(id);
            if (f != null) {
                cart.remove(f);
            }


            RequestDispatcher view = request
                    .getRequestDispatcher("/protected/main.jsp?action=cart");
            view.forward(request, response);
        }
    }
}
