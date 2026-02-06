package com.studentmgmt.filter;

import com.studentmgmt.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication and Authorization Filter.
 * Checks if user is logged in and has proper role-based access.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Allow access to static resources and login page
        if (isPublicResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Not logged in, redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Check role-based access
        String role = user.getRole();

        if (isAdminResource(uri) && !"admin".equals(role)) {
            // Non-admin trying to access admin resources
            httpResponse.sendRedirect(contextPath + "/dashboard");
            return;
        }

        if (isFacultyResource(uri) && !"faculty".equals(role) && !"admin".equals(role)) {
            // Non-faculty trying to access faculty resources
            httpResponse.sendRedirect(contextPath + "/dashboard");
            return;
        }

        if (isStudentResource(uri) && !"student".equals(role) && !"admin".equals(role)) {
            // Non-student trying to access student-only resources
            httpResponse.sendRedirect(contextPath + "/dashboard");
            return;
        }

        // User is authenticated and authorized, continue
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    /**
     * Check if the URI is a public resource (no authentication required).
     */
    private boolean isPublicResource(String uri) {
        return uri.endsWith("/login.jsp") ||
               uri.endsWith("/login") ||
               uri.contains("/css/") ||
               uri.contains("/js/") ||
               uri.contains("/images/") ||
               uri.endsWith(".css") ||
               uri.endsWith(".js") ||
               uri.endsWith(".png") ||
               uri.endsWith(".jpg") ||
               uri.endsWith(".gif") ||
               uri.endsWith(".ico");
    }

    /**
     * Check if the URI is an admin-only resource.
     */
    private boolean isAdminResource(String uri) {
        return uri.contains("/admin/") || uri.contains("/admin.");
    }

    /**
     * Check if the URI is a faculty-only resource.
     */
    private boolean isFacultyResource(String uri) {
        return uri.contains("/faculty/") || uri.contains("/faculty.");
    }

    /**
     * Check if the URI is a student-only resource.
     */
    private boolean isStudentResource(String uri) {
        return uri.contains("/student/") || uri.contains("/student.");
    }
}
