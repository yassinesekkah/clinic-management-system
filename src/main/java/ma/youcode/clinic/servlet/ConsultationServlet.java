package ma.youcode.clinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.youcode.clinic.factory.ServiceFactory;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.service.PatientService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/consultations", "/consultations/*"})
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
        // TRANCHE 1 (VOUS) : Traitement du formulaire et clôture (150 DH, statut TERMINEE)
        handleCloturerConsultation(request, response);
    }

    // =========================================================================
    // MÉTHODES DÉCOUPÉES PAR TRANCHE POUR ÉVITER LES CONFLITS GIT
    // =========================================================================

    /**
     * Tranche 2 (Coéquipier) : Récupère les patients du jour en attente et forwarde vers consultation-queue.jsp
     */
    private void handleQueue(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO (Coéquipier): Charger la file d'attente (getPatientsEnAttenteConsultationDuJour) et l'injecter
        request.getRequestDispatcher("/WEB-INF/views/doctor/consultation-queue.jsp").forward(request, response);
    }

    /**
     * Tranche 1 (Vous) : Récupère le patient par son ID et forwarde vers consultation-form.jsp
     */
    private void handleConsultationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO (Vous): Charger le patient avec ses constantes vitales et l'injecter dans la requête
        request.getRequestDispatcher("/WEB-INF/views/doctor/consultation-form.jsp").forward(request, response);
    }

    /**
     * Tranche 1 (Vous) : Valide les observations, diagnostic, traitement et clôture la consultation
     */
    private void handleCloturerConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO (Vous): Enregistrer la consultation, mettre à jour le statut du patient et rediriger
    }
}
