package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.ImprovementPlan;
import hr.javafx.eperformance.model.PerformanceMetrics;
import hr.javafx.eperformance.model.PerformanceReview;
import hr.javafx.eperformance.repository.EmployeeRepository;
import hr.javafx.eperformance.repository.ImprovementPlanRepository;
import hr.javafx.eperformance.repository.PerformanceReviewRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PerformanceReviewNewController {

    @FXML
    private ComboBox<String> performanceEmployeeComboBox;

    @FXML
    private ComboBox<String> performanceRatingComboBox;

    @FXML
    private ComboBox<String> performanceImprovementPlanComboBox;

    @FXML
    private TextField performanceReviewTextField;

    @FXML
    private TextField performanceReviewerTextField;

    @FXML
    private TextArea performanceCommentTextArea;

    @FXML
    private DatePicker performanceDatePicker;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final ImprovementPlanRepository improvementPlanRepository = new ImprovementPlanRepository();
    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();

    public void initialize() {
        populateComboBoxes();
        performanceRatingComboBox.getItems().addAll("1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5");
    }

    public void savePerformanceReview() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String employeeName = performanceEmployeeComboBox.getValue();
        if (employeeName == null) {
            errorMessage.append("Zaposlenik je obavezno polje.\n");
        }

        String rating = performanceRatingComboBox.getValue();
        if (rating == null) {
            errorMessage.append("Ocjena je obavezno polje.\n");
        }

        String improvementPlanDescription = performanceImprovementPlanComboBox.getValue();
        if (improvementPlanDescription == null) {
            errorMessage.append("Plan poboljšanja je obavezno polje.\n");
        }

        String review = performanceReviewTextField.getText();
        if (review.isEmpty()) {
            errorMessage.append("Pregled je obavezno polje.\n");
        }

        String reviewer = performanceReviewerTextField.getText();
        if (reviewer.isEmpty()) {
            errorMessage.append("Recenzent je obavezno polje.\n");
        }

        String comment = performanceCommentTextArea.getText();

        LocalDate date = performanceDatePicker.getValue();
        if (date == null) {
            errorMessage.append("Datum je obavezno polje.\n");
        } else if (date.isAfter(LocalDate.now())) {
            errorMessage.append("Datum ne može biti u budućnosti.\n");
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Pogreška pri unosu podataka");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        } else {
            Optional<Employee> employee = employeeRepository.findAll().stream()
                    .filter(e -> (e.getFirstName() + " " + e.getLastName()).equalsIgnoreCase(employeeName))
                    .findFirst();

            Optional<ImprovementPlan> improvementPlan = improvementPlanRepository.findAll().stream()
                    .filter(i -> i.getDescription().equalsIgnoreCase(improvementPlanDescription))
                    .findFirst();

            PerformanceReview performanceReview = new PerformanceReview.Builder()
                    .employee(employee.orElse(null))
                    .performanceMetrics(new PerformanceMetrics(Double.parseDouble(rating)))
                    .improvementPlan(improvementPlan.orElse(null))
                    .review(review)
                    .reviewer(reviewer)
                    .comment(comment)
                    .date(date)
                    .build();

            performanceReviewRepository.save(performanceReview);

            employee.ifPresent(e -> showSuccessDialog(e.getFirstName(), e.getLastName()));
            SceneManager.switchScene("/hr/javafx/eperformance/performanceReviewSearchScreen.fxml", "Pretraga ivzještaja", 900, 500);
        }
    }

    private void showSuccessDialog(String firstName, String lastName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje izvještaja");
        alert.setHeaderText("Izvještaj spremljen");
        alert.setContentText("Uspješno spremljen izvještaj za zaposlenika: " + firstName + " " + lastName + ".");
        alert.showAndWait();
    }


    private void populateComboBoxes() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            performanceEmployeeComboBox.getItems().add(employee.getFirstName() + " " + employee.getLastName());
        }

        List<ImprovementPlan> improvementPlans = improvementPlanRepository.findAll();
        for (ImprovementPlan improvementPlan : improvementPlans) {
            performanceImprovementPlanComboBox.getItems().add(improvementPlan.getDescription());
        }
    }


}
