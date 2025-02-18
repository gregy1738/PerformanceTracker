package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.ImprovementPlan;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.EmployeeRepository;
import hr.javafx.eperformance.repository.ImprovementPlanRepository;
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

public class ImprovementPlanSearchController {

    @FXML
    private ComboBox<String> improvementEmployeeComboBox;

    @FXML
    private DatePicker improvementEndDatePicker;

    @FXML
    private TableView<ImprovementPlan> improvementPlanTableView;

    @FXML
    private TableColumn<ImprovementPlan, String> improvementEmployeeTableColumn;

    @FXML
    private TableColumn<ImprovementPlan, String> improvementStartDateTableColumn;

    @FXML
    private TableColumn<ImprovementPlan, String> improvementEndDateTableColumn;

    @FXML
    private TableColumn<ImprovementPlan, String> improvementDescriptionTableColumn;

    @FXML
    private TableColumn<ImprovementPlan, String> improvementIdTableColumn;

    @FXML
    private Button deleteImprovementPlanButton;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final ImprovementPlanRepository improvementPlanDepository = new ImprovementPlanRepository();

    public void initialize() {
        User loggedInUser = SessionManager.getLoggedInUser();

        if(loggedInUser == null || !loggedInUser.getRole().equals(EmployeeType.DIRECTOR)){
            deleteImprovementPlanButton.setVisible(false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        populateComboBox();

        improvementEmployeeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployee().getFirstName()
                + " " + data.getValue().getEmployee().getLastName()));
        improvementStartDateTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartDate().format(formatter)));
        improvementEndDateTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndDate().format(formatter)));
        improvementDescriptionTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        improvementIdTableColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
    }

    public void filterImprovementPlans() {

        List<ImprovementPlan> improvementPlans = improvementPlanDepository.findAll();

        String employeeName = improvementEmployeeComboBox.getValue();

        if (employeeName != null){
            improvementPlans = improvementPlans.stream()
                    .filter(improvementPlan -> improvementPlan.getEmployee().getFirstName().equals(employeeName.split(" ")[0])
                            && improvementPlan.getEmployee().getLastName().equals(employeeName.split(" ")[1]))
                    .toList();
        }

        LocalDate endDate = improvementEndDatePicker.getValue();

        if(endDate != null){
            improvementPlans = improvementPlans.stream()
                    .filter(improvementPlan -> improvementPlan.getEndDate().isBefore(endDate))
                    .toList();
        }

        ObservableList<ImprovementPlan> improvementPlanObservableList = FXCollections.observableList(improvementPlans);
        improvementPlanTableView.setItems(improvementPlanObservableList);
        improvementEmployeeComboBox.setValue(null);

    }

    public void deleteImprovementPlan() {
        ImprovementPlan improvementPlan = improvementPlanTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje plana poboljšanja");
        alert.setContentText("Jeste li sigurni da želite obrisati plan poboljšanja za zaposlenika " + improvementPlan.getEmployee().getFirstName()
                + " " + improvementPlan.getEmployee().getLastName() + "?");
        alert.showAndWait();

        if(alert.getResult() != null && alert.getResult().getText().equals("OK")){
            improvementPlanDepository.delete(improvementPlan);
            filterImprovementPlans();
        } else {
            alert.close();
        }
    }

    public void editImprovementPlan() {
        ImprovementPlan improvementPlan = improvementPlanTableView.getSelectionModel().getSelectedItem();

        if (improvementPlan != null) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/eperformance/improvementPlanEditScreen.fxml"));
                SceneManager.switchScene(loader, "Uređivanje plana poboljšanja", 900, 500);
                ImprovementPlanEditController improvementPlanEditController = loader.getController();
                improvementPlanEditController.setImprovementPlan(improvementPlan);
            } catch (IOException e) {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }

    private void populateComboBox() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            improvementEmployeeComboBox.getItems().add(employee.getFirstName() + " " + employee.getLastName());
        }
    }

}
