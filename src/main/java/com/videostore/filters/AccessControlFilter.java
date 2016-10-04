package com.videostore.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by max on 16.09.2016.
 */
public class AccessControlFilter implements Filter {
    private FilterConfig config = null;
    private String loginPage;


    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        loginPage = config.getInitParameter("loginPage");
        if (loginPage == null) {
            throw new ServletException("loginPage init param is missing");
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletResponse httpResp = (HttpServletResponse) resp;
        HttpServletRequest httpReq = (HttpServletRequest) req;

        if (!isAuthenticated(httpReq)) {
            System.out.println("not auth");
            String forwardURI = getForwardURI(httpReq);

//          Forward to the login page and stop further processing
            ServletContext context = config.getServletContext();
            RequestDispatcher rd = context.getRequestDispatcher(forwardURI);
            if (rd == null) {
                httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Login page doesn't exist");
            }
            rd.forward(req, resp);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }

    private boolean isAuthenticated(HttpServletRequest request) {
        boolean isAuthenticated = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("validUser") != null) {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

    private String getForwardURI(HttpServletRequest request) {
        StringBuffer uri = new StringBuffer(request.getContextPath() + loginPage);
        return uri.toString();
    }
}
