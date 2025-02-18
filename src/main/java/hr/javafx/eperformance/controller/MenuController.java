package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import javafx.scene.control.Alert;

import java.io.IOException;

public class MenuController {

    public void showDepartmentSearchScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/departmentSearchScreen.fxml", "Pretraga odjela", 900, 500);
    }

    public void showDepartmentNewScreen() throws IOException {
        if (SessionManager.getLoggedInUser() == null || SessionManager.getLoggedInUser().getRole() != EmployeeType.DIRECTOR) {
            showErrorDialog();
            redirectToWelcomeScreen();
        } else {
            SceneManager.switchScene("/hr/javafx/eperformance/departmentNewScreen.fxml", "Dodaj odjel", 900, 500);
        }
    }

    public void showEmployeeSearchScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/employeeSearchScreen.fxml", "Pretraga zaposlenika", 900, 500);
    }

    public void showEmployeeNewScreen() throws IOException {
        if (SessionManager.getLoggedInUser() == null || SessionManager.getLoggedInUser().getRole() != EmployeeType.DIRECTOR) {
            showErrorDialog();
            redirectToWelcomeScreen();
        } else {
            SceneManager.switchScene("/hr/javafx/eperformance/employeeNewScreen.fxml", "Dodaj zaposlenika", 900, 500);
        }
    }

    public void showImprovementPlanSearchScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/improvementPlanSearchScreen.fxml", "Pretraga planova poboljšanja", 900, 500);
    }

    public void showImprovementPlanNewScreen() throws IOException {
        if (SessionManager.getLoggedInUser() == null || SessionManager.getLoggedInUser().getRole() != EmployeeType.DIRECTOR) {
            showErrorDialog();
            redirectToWelcomeScreen();
        } else {
            SceneManager.switchScene("/hr/javafx/eperformance/improvementPlanNewScreen.fxml", "Dodaj plan poboljšanja", 900, 500);
        }
    }

    public void showPerformanceReviewSearchScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/performanceReviewSearchScreen.fxml", "Pretraga ocjena", 900, 500);
    }

    public void showPerformanceReviewNewScreen() throws IOException {
        if (SessionManager.getLoggedInUser() == null || SessionManager.getLoggedInUser().getRole() != EmployeeType.DIRECTOR) {
            showErrorDialog();
            redirectToWelcomeScreen();
        } else {
            SceneManager.switchScene("/hr/javafx/eperformance/performanceReviewNewScreen.fxml", "Dodaj ocjenu", 900, 500);
        }
    }

    public void showChangeLogScreen() throws IOException {
        if(SessionManager.getLoggedInUser() == null || SessionManager.getLoggedInUser().getRole() != EmployeeType.DIRECTOR) {
            showErrorDialog();
            redirectToWelcomeScreen();
        } else {
            SceneManager.switchScene("/hr/javafx/eperformance/changeLogScreen.fxml", "Povijest promjena", 900, 500);
        }
    }


    private static void redirectToWelcomeScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/welcomeScreen.fxml","Dobrodošli", 900, 500);
    }

    private static void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Nemate ovlasti");
        alert.setContentText("Samo direktor može dodavati nove zapise.");
        alert.showAndWait();
    }

}
