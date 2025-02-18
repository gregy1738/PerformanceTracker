package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.ImprovementPlan;
import hr.javafx.eperformance.repository.EmployeeRepository;
import hr.javafx.eperformance.repository.ImprovementPlanRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ImprovementPlanNewController {

    @FXML
    private ComboBox<String> improvementEmployeeComboBox;

    @FXML
    private TextArea improvementDescriptionTextArea;

    @FXML
    private DatePicker improvementStartDatePicker;

    @FXML
    private DatePicker improvementEndDatePicker;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final ImprovementPlanRepository improvementPlanRepository = new ImprovementPlanRepository();

    public void initialize() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            improvementEmployeeComboBox.getItems().add(employee.getFirstName() + " " + employee.getLastName());
        }
    }

    public void saveImprovementPlan() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String description = improvementDescriptionTextArea.getText();
        if (description.isEmpty()) {
            errorMessage.append("Opis je obavezno polje.\n");
        }

        String employeeName = improvementEmployeeComboBox.getValue();
        if (employeeName == null) {
            errorMessage.append("Zaposlenik je obavezno polje.\n");
        }

        LocalDate startDate = improvementStartDatePicker.getValue();
        if (startDate == null) {
            errorMessage.append("Datum početka je obavezno polje.\n");
        } else if (startDate.isBefore(LocalDate.now())) {
            errorMessage.append("Datum početka ne može biti u prošlosti.\n");
        }

        LocalDate endDate = improvementEndDatePicker.getValue();
        if (endDate == null) {
            errorMessage.append("Datum završetka je obavezno polje.\n");
        } else if (endDate.isBefore(startDate)) {
            errorMessage.append("Datum završetka ne može biti prije datuma početka.\n");
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Pogreška prilikom spremanja plana poboljšanja");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        } else {
            Optional<Employee> employee = employeeRepository.findAll().stream()
                    .filter(e -> (e.getFirstName() + " " + e.getLastName()).equalsIgnoreCase(employeeName))
                    .findFirst();
            ImprovementPlan improvementPlan = new ImprovementPlan(null, employee.orElse(null), description, null, improvementStartDatePicker.getValue(), improvementEndDatePicker.getValue());
            improvementPlanRepository.save(improvementPlan);

            employee.ifPresent(e -> showSuccessDialog(e.getFirstName(), e.getLastName()));
            SceneManager.switchScene("/hr/javafx/eperformance/improvementPlanSearchScreen.fxml", "Pretraga planova poboljšanja", 900, 500);
        }
    }

    private void showSuccessDialog(String firstName, String lastName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje plana poboljšanja");
        alert.setHeaderText("Plan poboljšanja spremljen");
        alert.setContentText("Uspješno spremljen plan poboljšanja za zaposlenika: " + firstName + " " + lastName + ".");
        alert.showAndWait();
    }

}
