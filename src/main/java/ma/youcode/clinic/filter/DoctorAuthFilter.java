package ma.youcode.clinic.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.model.enums.*;

@WebFilter(urlPatterns = { "/consultations", "/consultations/*" })
public class DoctorAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request2 = (HttpServletRequest) request;

        HttpServletResponse response2 = (HttpServletResponse) response;

        HttpSession session = request2.getSession(false);

        String method = request2.getMethod();
        String pathInfo = request2.getPathInfo(); 
        
        // 1. Any user accessing /consultations must be logged in
        if (session == null || session.getAttribute("userId") == null) {
            response2.sendRedirect(request2.getContextPath() + "/login");
            return;
        }

        Role role = (Role) session.getAttribute("role");

        // 2. Starting or closing a consultation is restricted strictly to the doctor
        boolean isDoctorOnly = "POST".equalsIgnoreCase(method) || "/nouvelle".equals(pathInfo);

        if (isDoctorOnly && role != Role.GENERALISTE) {
            response2.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request2, response2);
    }

}
