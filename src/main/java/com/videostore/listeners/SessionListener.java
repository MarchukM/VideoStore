package com.videostore.listeners;

import com.videostore.domain.Cart;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by max on 22.09.2016.
 */
public class SessionListener implements HttpSessionListener {
    public static final String SHOPPING_CART = "shoppingCart";

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute(SHOPPING_CART, new Cart());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
