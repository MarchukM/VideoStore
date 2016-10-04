package com.videostore.listeners;

/**
 * Created by max on 16.09.2016.
 */

import com.videostore.mysql.MySqlDaoFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ResourceManagerListener implements ServletContextListener{

    public static final String MYSQL_DAO_FACTORY = "daoFactory";

    public ResourceManagerListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {

        ServletContext application = sce.getServletContext();
        application.setAttribute(MYSQL_DAO_FACTORY, new MySqlDaoFactory());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        application.removeAttribute(MYSQL_DAO_FACTORY);
    }

}
