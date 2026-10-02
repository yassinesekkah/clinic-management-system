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

    @Override
    public void init() {
        this.patientService = ServiceFactory.getPatientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        // Route: Afficher le formulaire d'admission
        if ("/nouveau".equals(pathInfo) || "nouveau".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/nurse/patient-form.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");

        // Recherche par ID
        if (idParam != null && !idParam.trim().isEmpty()) {
            request.setAttribute("IDsearch", true);
            try {
                Long id = Long.parseLong(idParam.trim());
                Optional<Patient> foundedPatient = patientService.getPatientById(id);

                if (foundedPatient.isPresent()) {
                    Patient p = foundedPatient.get();
                    request.setAttribute("foundedPatient", p);
                    request.setAttribute("patients", List.of(p));
                } else {
                    request.setAttribute("foundedPatient", null);
                    request.setAttribute("patients", Collections.emptyList());
                    request.setAttribute("errorMessage", "Aucun patient trouvé avec l'identifiant : " + id);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("foundedPatient", null);
                request.setAttribute("patients", Collections.emptyList());
                request.setAttribute("errorMessage", "Format d'identifiant invalide : '" + idParam + "'");
            }
        } else {
            // Route par défaut: Afficher la file d'attente des patients du jour
            request.setAttribute("IDsearch", false);
            List<Patient> patientsDuJour = patientService.getPatientsDuJour();
            request.setAttribute("patients", patientsDuJour);

            if ("true".equals(request.getParameter("success"))) {
                request.setAttribute("successMessage", "Le patient a été enregistré avec succès et ajouté à la file d'attente.");
            }
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
            patientService.addPatient(patient);
            // Pattern Post-Redirect-Get pour empêcher la double soumission du formulaire
            response.sendRedirect(request.getContextPath() + "/patients?success=true");
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
