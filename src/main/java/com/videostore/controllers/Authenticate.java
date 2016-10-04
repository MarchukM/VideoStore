package com.videostore.controllers;

import com.videostore.dao.UserDao;
import com.videostore.domain.User;
import com.videostore.mysql.MySqlDaoFactory;
import com.videostore.utilities.TextUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Authenticate extends HttpServlet {
    private static final Logger log = Logger
            .getLogger(com.videostore.controllers.Authenticate.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        Map<String, String> messages = new HashMap<>();
        MySqlDaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                .getAttribute("daoFactory");

        if (TextUtil.isEmpty(userName)) {
            messages.put("userName", "Please enter your user name.");
        }
        if (TextUtil.isEmpty(password)) {
            messages.put("password", "Please enter a password.");
        }


        User user = null;
        if (messages.isEmpty()) {
            try (Connection con = daoFactory.getConnection()) {
                UserDao dao = daoFactory.getUserDao(con);
                user = dao.getByCredentials(userName, password);
            } catch (SQLException e) {
                log.error(e);
            }
        } else {
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("index.jsp")
                    .forward(request, response);
        }

        if (user == null) {
            messages.put("userName", "Wrong username or password.");
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("index.jsp")
                    .forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("validUser", user);
            response.sendRedirect("/protected/main.jsp?action=filmList");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/index.jsp");
    }
}
