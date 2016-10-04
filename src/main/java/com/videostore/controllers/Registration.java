package com.videostore.controllers;

import com.videostore.dao.DaoFactory;
import com.videostore.dao.UserDao;
import com.videostore.domain.User;
import com.videostore.mysql.MySqlDaoFactory;
import com.videostore.utilities.TextUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 18.09.2016.
 */
public class Registration extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPass");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        Map<String, String> messages = new HashMap<>();

        if (TextUtil.isEmpty(userName)) {
            messages.put("userName", "Please, enter a user name.");
        }

        if (TextUtil.isEmpty(password)) {
            messages.put("password", "Please, enter a password.");
        }

        if (TextUtil.isEmpty(confirmPass)) {
            messages.put("confirmPass", "Confirm your password.");
        }

        if (TextUtil.isEmpty(firstName)) {
            messages.put("firstName", "Please enter your first name.");
        }

        if (TextUtil.isEmpty(lastName)) {
            messages.put("lastName", "Please enter your last name.");
        }

        if (TextUtil.isEmpty(email)) {
            messages.put("email", "Please, enter your email.");
        }

        if (!password.equals(confirmPass)) {
            messages.put("confirmPass", "Wrong confirmation.");
        }

        if (!messages.isEmpty()) {
            request.setAttribute("userName", userName);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("index.jsp?action=registration").forward(request, response);
            return;
        }

        User newUser = null;
        User preparedUser = new User();
        preparedUser.setUserName(userName);
        preparedUser.setFirstName(firstName);
        preparedUser.setLastName(lastName);
        preparedUser.setPassword(password);
        preparedUser.setEmail(email);


        DaoFactory dao = (MySqlDaoFactory) getServletContext().getAttribute("daoFactory");
        try (Connection con = dao.getConnection()) {
            UserDao userDao = dao.getUserDao(con);
            newUser = userDao.create(preparedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (newUser == null) {
            messages.put("userName", "This user name is already taken.");
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("/index.jsp?action=registration").forward(request, response);
        }else{
            response.reset();
            response.sendRedirect("/index.jsp?action=success");
    }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}