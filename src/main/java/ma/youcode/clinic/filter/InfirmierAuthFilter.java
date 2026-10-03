package ma.youcode.clinic.filter;

import java.io.IOException;

import ma.youcode.clinic.model.enums.Role;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/patients", "/patients/*"})
public class InfirmierAuthFilter implements Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request2 =
                    (HttpServletRequest) request;

        HttpServletResponse response2 =
            (HttpServletResponse) response;

        HttpSession session = request2.getSession(false);

        if(session == null || session.getAttribute("userId") == null){
                    response2.sendRedirect(request2.getContextPath() + "/login");
                    return;
        }
        Role role = (Role) session.getAttribute("role");

        if(role != Role.INFIRMIER){
            response2.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request2, response2);
    }
}
