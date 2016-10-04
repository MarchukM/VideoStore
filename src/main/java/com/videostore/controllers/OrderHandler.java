package com.videostore.controllers;

import com.videostore.dao.DaoFactory;
import com.videostore.dao.OrderDao;
import com.videostore.domain.Cart;
import com.videostore.domain.Order;
import com.videostore.domain.User;
import com.videostore.mysql.MySqlDaoFactory;
import com.videostore.utilities.TextUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderHandler extends HttpServlet {
    private static final Logger log = Logger
            .getLogger(com.videostore.controllers.Authenticate.class);
    public static final int RECORDS_PER_PAGE = 2;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String telephone = request.getParameter("telephone");

        Map<String, String> messages = new HashMap<>();

        if(TextUtil.isEmpty(name)){
            messages.put("name", "Please, enter your name.");
        }

        if(TextUtil.isEmpty(address)){
            messages.put("address", "Please, enter your address");
        }

        if(TextUtil.isEmpty(telephone)){
            messages.put("telephone", "Please, enter your telephone number");
        }

        if(messages.isEmpty()) {
            MySqlDaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                    .getAttribute("daoFactory");

            Order order = new Order();
            HttpSession session;
            session = request.getSession();
            Cart currentCart = (Cart) session.getAttribute("shoppingCart");
            User currentUser = (User) session.getAttribute("validUser");

            order.setName(name);
            order.setAddress(address);
            order.setTelephone(telephone);
            order.setUserId(currentUser.getId());
            order.setOrderedFilms(currentCart.getFilms());


            try (Connection conection = daoFactory.getConnection()) {
                OrderDao dao = daoFactory.getOrderDao(conection);
                dao.create(order);
            } catch (SQLException e) {
                log.error(e);
            }
            response.sendRedirect("/protected/main.jsp?action=gratitude");
        } else{
            request.setAttribute("messages", messages);
            RequestDispatcher view = request
                    .getRequestDispatcher("protected/main.jsp?action=checkout");
            view.forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        DaoFactory daoFactory = (MySqlDaoFactory) getServletContext()
                .getAttribute("daoFactory");

        if(action.equalsIgnoreCase("list")) {
            int page = 1;

            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            try (Connection connection = daoFactory.getConnection()) {
                OrderDao dao = daoFactory.getOrderDao(connection);
                ArrayList<Order> orders = dao
                        .getPart((page - 1) * RECORDS_PER_PAGE, RECORDS_PER_PAGE);
                int noOfRecords = dao.getNoOfRecords();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / RECORDS_PER_PAGE);

                request.setAttribute("orderList", orders);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("currentPage", page);

                RequestDispatcher view = request
                        .getRequestDispatcher("/protected/main.jsp?mode=admin&action=listOfOrders");
                view.forward(request, response);

            } catch (SQLException e) {
               log.error(e);
            }
        }

        if(action.equalsIgnoreCase("details")){
            try(Connection connection = daoFactory.getConnection()){
                OrderDao dao = daoFactory.getOrderDao(connection);

                String parameter = request.getParameter("id");
                Integer orderId = Integer.valueOf(parameter);

                Order order = dao.getById(orderId);

                request.setAttribute("order", order);

                RequestDispatcher view = request
                        .getRequestDispatcher("/protected/main.jsp?mode=admin&action=details");
                view.forward(request, response);
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
}
