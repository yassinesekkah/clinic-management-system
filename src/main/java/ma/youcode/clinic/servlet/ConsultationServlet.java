package ma.youcode.clinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.factory.ServiceFactory;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.service.PatientService;
import ma.youcode.clinic.model.Consultation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = { "/consultations", "/consultations/*" })
public class ConsultationServlet extends HttpServlet {

    private ConsultationService consultationService;
    private PatientService patientService;

    @Override
    public void init() {
        this.consultationService = ServiceFactory.getConsultationService();
        this.patientService = ServiceFactory.getPatientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/nouvelle".equals(pathInfo)) {
            // TRANCHE 1 (VOUS) : Afficher le formulaire de consultation pour un patient
            handleConsultationForm(request, response);
        } else {
            // TRANCHE 2 (COÉQUIPIER) : Afficher la file d'attente des patients du jour
            handleQueue(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TRANCHE 1 (VOUS) : Traitement du formulaire et clôture (150 DH, statut
        // TERMINEE)
        handleCloturerConsultation(request, response);
    }

    // =========================================================================
    // MÉTHODES DÉCOUPÉES PAR TRANCHE POUR ÉVITER LES CONFLITS GIT
    // =========================================================================

    /**
     * Tranche 2 (Coéquipier) : Récupère les patients du jour en attente et forwarde
     * vers consultation-queue.jsp
     */
    private void handleQueue(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Consultation> consultations = consultationService.getConsultationsEnAttenteDuJour();

        request.setAttribute("consultations", consultations);

        request.getRequestDispatcher(
                "/WEB-INF/views/doctor/consultation-queue.jsp").forward(request, response);
    }

    /**
     * Tranche 1 (Vous) : Récupère le patient par son ID et forwarde vers
     * consultation-form.jsp
     */
    private void handleConsultationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long patientId = Long.parseLong(request.getParameter("patientId"));
        Optional<Patient> patient = patientService.getPatientById(patientId);
        if (patient.isPresent()) {
            request.setAttribute("patient", patient.get());
        } else {
            response.sendRedirect(request.getContextPath() + "/consultations?error=not_found");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/doctor/consultation-form.jsp").forward(request, response);
    }

    /**
     * Tranche 1 (Vous) : Valide les observations, diagnostic, traitement et clôture
     * la consultation
     */
    private void handleCloturerConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Verify CSRF Token
        HttpSession session = request.getSession(false);
        String submittedToken = request.getParameter("csrfToken");
        if (session == null || submittedToken == null || !submittedToken.equals(session.getAttribute("csrfToken"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Jeton CSRF invalide ou session expirée.");
            return;
        }

        // 2. Read fields by their HTML 'name' attribute
        String patientIdStr = request.getParameter("patientId");
        String motif = request.getParameter("motif");
        String observations = request.getParameter("observations");
        String diagnostic = request.getParameter("diagnostic");
        String traitement = request.getParameter("traitement");
        // 3. Convert types and validate
        Long patientId = Long.parseLong(patientIdStr);

        // 4. Retrieve logged-in doctor ID from session (NEVER pass doctorId in a hidden form field!)
        Long medecinId = (Long) session.getAttribute("userId");
        consultationService.cloturerConsultation(patientId, medecinId, motif, observations, diagnostic, traitement);
        response.sendRedirect(request.getContextPath() + "/consultations");
    }
}
