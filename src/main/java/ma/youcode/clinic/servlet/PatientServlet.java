package ma.youcode.clinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.youcode.clinic.exception.ValidationException;
import ma.youcode.clinic.factory.ServiceFactory;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.PatientStatus;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.service.PatientService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/patients", "/patients/*"})
public class PatientServlet extends HttpServlet {

    private PatientService patientService;
    private ConsultationService consultationService;

    @Override
    public void init() {
        this.patientService = ServiceFactory.getPatientService();
        this.consultationService = ServiceFactory.getConsultationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        // Action: Créer une consultation en attente pour un patient existant
        if ("creerConsultation".equals(action)) {
            String patientIdStr = request.getParameter("patientId");
            if (patientIdStr != null && !patientIdStr.isBlank()) {
                try {
                    Long pId = Long.parseLong(patientIdStr.trim());
                    consultationService.creerConsultationEnAttente(pId);
                    response.sendRedirect(request.getContextPath() + "/patients?success=consultation_creee");
                    return;
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Identifiant patient invalide.");
                }
            }
        }

        // Route: Afficher le formulaire d'admission
        if ("/nouveau".equals(pathInfo) || "nouveau".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/nurse/patient-form.jsp").forward(request, response);
            return;
        }

        String searchQuery = request.getParameter("search");
        if (searchQuery == null || searchQuery.isBlank()) {
            searchQuery = request.getParameter("id");
        }

        // Recherche multi-critères (Nom, Prénom, SSN ou ID)
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            request.setAttribute("IDsearch", true);
            request.setAttribute("searchQuery", searchQuery.trim());
            List<Patient> foundPatients = patientService.searchPatients(searchQuery.trim());
            request.setAttribute("patients", foundPatients);

            if (foundPatients.isEmpty()) {
                request.setAttribute("errorMessage", "Aucun patient trouvé pour la recherche : '" + searchQuery.trim() + "'");
            }
        } else {
            // Route par défaut: Afficher l'ensemble des patients enregistrés dans la clinique
            request.setAttribute("IDsearch", false);
            List<Patient> allPatients = patientService.getAllPatients();
            request.setAttribute("patients", allPatients);
        }

        // Injecter les IDs des patients ayant déjà une consultation aujourd'hui (en attente ou terminée)
        request.setAttribute("patientsEnAttenteAujourdhui", consultationService.getPatientIdsWithConsultationEnAttenteDuJour());
        request.setAttribute("patientsTerminesAujourdhui", consultationService.getPatientIdsWithConsultationTermineeDuJour());

        if ("true".equals(request.getParameter("success"))) {
            request.setAttribute("successMessage", "Le patient a été enregistré avec succès et sa consultation a été créée.");
        } else if ("patient_cree".equals(request.getParameter("success"))) {
            request.setAttribute("successMessage", "Le patient a été enregistré avec succès.");
        } else if ("consultation_creee".equals(request.getParameter("success"))) {
            request.setAttribute("successMessage", "Une consultation en attente a été ouverte pour le patient sélectionné.");
        }

        request.getRequestDispatcher("/WEB-INF/views/nurse/patient-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        List<String> errors = new ArrayList<>();

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateNaissanceStr = request.getParameter("dateNaissance");
        String ssn = request.getParameter("numeroSecuriteSociale");
        String tension = request.getParameter("tensionArterielle");
        String frequenceCardiaqueStr = request.getParameter("frequenceCardiaque");
        String temperatureStr = request.getParameter("temperature");
        String frequenceRespiratoireStr = request.getParameter("frequenceRespiratoire");

        LocalDate dateNaissance = null;
        if (dateNaissanceStr != null && !dateNaissanceStr.isBlank()) {
            try {
                dateNaissance = LocalDate.parse(dateNaissanceStr.trim());
            } catch (DateTimeParseException e) {
                errors.add("Format de la date de naissance invalide (attendu: AAAA-MM-JJ).");
            }
        }

        int frequenceCardiaque = 0;
        if (frequenceCardiaqueStr != null && !frequenceCardiaqueStr.isBlank()) {
            try {
                frequenceCardiaque = Integer.parseInt(frequenceCardiaqueStr.trim());
            } catch (NumberFormatException e) {
                errors.add("La fréquence cardiaque doit être un nombre entier valide.");
            }
        }

        int frequenceRespiratoire = 0;
        if (frequenceRespiratoireStr != null && !frequenceRespiratoireStr.isBlank()) {
            try {
                frequenceRespiratoire = Integer.parseInt(frequenceRespiratoireStr.trim());
            } catch (NumberFormatException e) {
                errors.add("La fréquence respiratoire doit être un nombre entier valide.");
            }
        }

        BigDecimal temperature = null;
        if (temperatureStr != null && !temperatureStr.isBlank()) {
            try {
                temperature = new BigDecimal(temperatureStr.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                errors.add("La température corporelle doit être un nombre décimal valide (ex: 37.2).");
            }
        }

        Patient patient = new Patient(
                nom,
                prenom,
                dateNaissance,
                ssn,
                tension,
                frequenceCardiaque,
                temperature,
                frequenceRespiratoire
        );

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("patient", patient);
            request.getRequestDispatcher("/WEB-INF/views/nurse/patient-form.jsp").forward(request, response);
            return;
        }

        try {
            Patient savedPatient = patientService.addPatient(patient);

            boolean autoConsultation = "true".equalsIgnoreCase(request.getParameter("autoConsultation"))
                    || "on".equalsIgnoreCase(request.getParameter("autoConsultation"));

            if (autoConsultation) {
                consultationService.creerConsultationEnAttente(savedPatient.getId());
                response.sendRedirect(request.getContextPath() + "/patients?success=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/patients?success=patient_cree");
            }
            return;
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getErrors());
            request.setAttribute("patient", patient);
            request.getRequestDispatcher("/WEB-INF/views/nurse/patient-form.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errors", List.of("Erreur lors de l'enregistrement: " + e.getMessage()));
            request.setAttribute("patient", patient);
            request.getRequestDispatcher("/WEB-INF/views/nurse/patient-form.jsp").forward(request, response);
        }
    }
}
