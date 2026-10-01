package ma.youcode.clinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.factory.ServiceFactory;
import ma.youcode.clinic.model.Utilisateur;
import ma.youcode.clinic.service.AuthService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Creer un token pour proteger le formulaire.
        HttpSession session = request.getSession();
        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. Verifier le token avant de traiter le formulaire.
        HttpSession session = request.getSession(false);
        String token = request.getParameter("csrfToken");
        if (session == null || token == null || !token.equals(session.getAttribute("csrfToken"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide.");
            return;
        }

        // 2. Lire les champs et demander au service de verifier les identifiants.
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Optional<Utilisateur> resultat = authService.login(email, password);

        // 3. Afficher la meme erreur pour un email inconnu ou un mauvais mot de passe.
        if (resultat.isEmpty()) {
            request.setAttribute("error", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // 4. Changer l'identifiant de session et garder uniquement les informations utiles.
        Utilisateur utilisateur = resultat.get();
        request.changeSessionId();
        session.setAttribute("userId", utilisateur.getId());
        session.setAttribute("role", utilisateur.getRole());
        session.setAttribute("csrfToken", UUID.randomUUID().toString());

        // Les pages propres a chaque role seront ajoutees ensuite.
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
