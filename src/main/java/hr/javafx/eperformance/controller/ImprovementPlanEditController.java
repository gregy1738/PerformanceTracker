package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.ChangeLog;
import hr.javafx.eperformance.model.ImprovementPlan;
import hr.javafx.eperformance.repository.ChangeLogRepository;
import hr.javafx.eperformance.repository.ImprovementPlanRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ImprovementPlanEditController {

    @FXML
    private DatePicker improvementStartDatePicker;

    @FXML
    private DatePicker improvementEndDatePicker;

    private final ImprovementPlanRepository improvementPlanRepository = new ImprovementPlanRepository();
    private Long id;

    public void setImprovementPlan(ImprovementPlan improvementPlan) {
        id = improvementPlan.getId();
        improvementStartDatePicker.setValue(improvementPlan.getStartDate());
        improvementEndDatePicker.setValue(improvementPlan.getEndDate());
    }

    public void confirmEdit() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        LocalDate startDate = improvementStartDatePicker.getValue();
        if (startDate == null) {
            errorMessage.append("Unesite početni datum plana poboljšanja!\n");
        } else if (startDate.isBefore(LocalDate.now())) {
            errorMessage.append("Početni datum plana poboljšanja ne može biti u prošlosti!\n");
        }

        LocalDate endDate = improvementEndDatePicker.getValue();
        if (endDate == null) {
            errorMessage.append("Unesite završni datum plana poboljšanja!\n");
        } else if (endDate.isBefore(startDate)) {
            errorMessage.append("Završni datum plana poboljšanja ne može biti prije početnog datuma!\n");
        }

        Alert alert = confirmationAlert();

        if (!errorMessage.isEmpty()) {
            errorAlert(alert, errorMessage);

        } else if (alert.getResult().getText().equals("OK")) {
            Optional<ImprovementPlan> oldPlanOptional = improvementPlanRepository.findById(id);
            EmployeeType changedByRole = SessionManager.getLoggedInUser().getRole();
            String changedByEmail = SessionManager.getLoggedInUser().getEmail();

            ImprovementPlan newPlan = new ImprovementPlan(id, startDate, endDate);

            if (oldPlanOptional.isPresent()) {
                ImprovementPlan oldPlan = oldPlanOptional.get();
                changedValuesCheck(oldPlan, newPlan, changedByRole, changedByEmail);
            }

            improvementPlanRepository.update(newPlan);
            SceneManager.switchScene("/hr/javafx/eperformance/improvementPlanSearchScreen.fxml", "Pretraga plana poboljšanja", 900, 500);
        } else {
            alert.close();
        }
    }

    private static void errorAlert(Alert alert, StringBuilder errorMessage) {
        alert.close();
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Greška");
        errorAlert.setHeaderText("Neispravan unos");
        errorAlert.setContentText(errorMessage.toString());
        errorAlert.showAndWait();
    }

    private static Alert confirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja plana za poboljšanje");
        alert.setContentText("Jeste li sigurni da želite urediti plan za poboljšanje?");
        alert.showAndWait();
        return alert;
    }

    private static void changedValuesCheck(ImprovementPlan oldPlan, ImprovementPlan newPlan, EmployeeType changedByRole, String changedByEmail) {
        if (!oldPlan.getStartDate().equals(newPlan.getStartDate())) {
            ChangeLogRepository.saveChange(new ChangeLog("Početni datum plana",
                    oldPlan.getStartDate().toString(), newPlan.getStartDate().toString(), changedByEmail, changedByRole));
        }
        if (!oldPlan.getEndDate().equals(newPlan.getEndDate())) {
            ChangeLogRepository.saveChange(new ChangeLog("Završni datum plana",
                    oldPlan.getEndDate().toString(), newPlan.getEndDate().toString(), changedByEmail, changedByRole));
        }
    }

}
