package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.PerformanceReview;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.EmployeeRepository;
import hr.javafx.eperformance.repository.PerformanceReviewRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PerformanceReviewSearchController {

    @FXML
    private ComboBox<String> performanceEmployeeComboBox;

    @FXML
    private DatePicker performanceDatePicker;

    @FXML
    private TableView<PerformanceReview> performanceReviewTableView;

    @FXML
    private TableColumn<PerformanceReview, String> performanceEmployeeTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceDateTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceReviewTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceReviewerTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceIdTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceRatingTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceCommentTableColumn;

    @FXML
    private TableColumn<PerformanceReview, String> performanceImprovementPlanTableColumn;

    @FXML
    private Button deletePerformanceReviewButton;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();

    public void initialize() {
        User loggedInUser = SessionManager.getLoggedInUser();

        if(loggedInUser == null || !loggedInUser.getRole().equals(EmployeeType.DIRECTOR)){
            deletePerformanceReviewButton.setVisible(false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        populateComboBox();

        performanceEmployeeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployee().getFirstName()
                + " " + data.getValue().getEmployee().getLastName()));
        performanceDateTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().format(formatter)));
        performanceReviewTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReview()));
        performanceReviewerTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReviewer()));
        performanceIdTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        performanceRatingTableColumn.setCellValueFactory(data -> new SimpleStringProperty
                (String.valueOf(data.getValue().getPerformanceMetrics().getPerformanceRating())));
        performanceCommentTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComment()));
        performanceImprovementPlanTableColumn.setCellValueFactory(data -> new SimpleStringProperty
                (data.getValue().getImprovementPlan() == null ? "Nije pridodan plan poboljšanja" : data.getValue().getImprovementPlan().getDescription()));
    }

    public void filterPerformanceReviews(){

        List<PerformanceReview> performanceReviews = performanceReviewRepository.findAll();

        String employeeName = performanceEmployeeComboBox.getValue();

        if (employeeName != null){
            performanceReviews = performanceReviews.stream()
                    .filter(improvementPlan -> improvementPlan.getEmployee().getFirstName().equals(employeeName.split(" ")[0])
                            && improvementPlan.getEmployee().getLastName().equals(employeeName.split(" ")[1]))
                    .toList();
        }

        LocalDate date = performanceDatePicker.getValue();

        if(date != null){
            performanceReviews = performanceReviews.stream()
                    .filter(performanceReview -> performanceReview.getDate().equals(date))
                    .toList();
        }

        ObservableList<PerformanceReview> performanceReviewObservableList = FXCollections.observableList(performanceReviews);
        performanceReviewTableView.setItems(performanceReviewObservableList);
        performanceEmployeeComboBox.setValue(null);
    }

    public void deletePerformanceReview(){
        PerformanceReview performanceReview = performanceReviewTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje izvještaja");
        alert.setContentText("Jeste li sigurni da želite obrisati izvještaj za zaposlenika " + performanceReview.getEmployee().getFirstName()
                + " " + performanceReview.getEmployee().getLastName() + "?");
        alert.showAndWait();

        if(alert.getResult() != null && alert.getResult().getText().equals("OK")){
            performanceReviewRepository.delete(performanceReview);
            filterPerformanceReviews();
        } else {
            alert.close();
        }
    }

    public void editPerformanceReview() {
        PerformanceReview performanceReview = performanceReviewTableView.getSelectionModel().getSelectedItem();

        if (performanceReview != null) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/eperformance/performanceReviewEditScreen.fxml"));
                SceneManager.switchScene(loader, "Uređivanje izvještaja", 900, 500);
                PerformanceReviewEditController performanceReviewEditController = loader.getController();
                performanceReviewEditController.setPerformanceReview(performanceReview);
            } catch (IOException e) {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }

    private void populateComboBox() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            performanceEmployeeComboBox.getItems().add(employee.getFirstName() + " " + employee.getLastName());
        }
    }

}
