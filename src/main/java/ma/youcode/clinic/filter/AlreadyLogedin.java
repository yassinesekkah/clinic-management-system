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
import ma.youcode.clinic.service.AuthService;

@WebFilter(urlPatterns = { "/login" })
public class AlreadyLogedin implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request2 = (HttpServletRequest) request;

        HttpServletResponse response2 = (HttpServletResponse) response;

        HttpSession session = request2.getSession(false);

        if (session != null && session.getAttribute("userId") != null) {
            Role userRole = (Role) session.getAttribute("role");
            switch (userRole) {
                case INFIRMIER -> response2.sendRedirect(request2.getContextPath() + "/patients");
                case GENERALISTE -> response2.sendRedirect(request2.getContextPath() + "/consultations");
                default -> response2.sendRedirect(request2.getContextPath() + "/home");
            }
            return;
        }

        chain.doFilter(request2, response2);
    }

}
