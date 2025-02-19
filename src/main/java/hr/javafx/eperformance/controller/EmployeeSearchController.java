package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.helper.ThreadHelper;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.DepartmentRepository;
import hr.javafx.eperformance.repository.EmployeeRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class EmployeeSearchController {

    @FXML
    private TextField employeeNameTextField;

    @FXML
    private TextField employeeSalaryFromTextField;

    @FXML
    private TextField employeeSalaryToTextField;

    @FXML
    private ComboBox<String> employeeDepartmentComboBox;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, String> employeeFirstNameColumn;

    @FXML
    private TableColumn<Employee, String> employeeLastNameColumn;

    @FXML
    private TableColumn<Employee, String> employeeJobTitleColumn;

    @FXML
    private TableColumn<Employee, String> employeeSalaryColumn;

    @FXML
    private TableColumn<Employee, String> employeeDepartmentColumn;

    @FXML
    private TableColumn<Employee, String> employeeIdColumn;

    @FXML
    private Button deleteEmployeeButton;

    @FXML
    private Label employeePerformanceRatingLabel;

    @FXML
    private Label employeeUpdatePerformanceRatingLabel;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final ThreadHelper threadHelper = new ThreadHelper();

    public void initialize() {
        User loggedInUser = SessionManager.getLoggedInUser();

        if(loggedInUser == null || !loggedInUser.getRole().equals(EmployeeType.DIRECTOR)){
            deleteEmployeeButton.setVisible(false);
        }

        employeeIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        employeeFirstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        employeeLastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        employeeJobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        employeeSalaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSalary())));
        employeeDepartmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty
                (cellData.getValue().getDepartment() != null ? cellData.getValue().getDepartment().getName() : "Zaposleniku nije dodijeljen odjel"));

        populateComboBox();
        threadHelper.highestPerformanceRating(employeePerformanceRatingLabel);
        threadHelper.updatePerformanceRating(employeeUpdatePerformanceRatingLabel);
    }

    public void filterEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        String employeeName = employeeNameTextField.getText();
        String employeeSalaryFromString = employeeSalaryFromTextField.getText();
        String employeeSalaryToString = employeeSalaryToTextField.getText();
        String employeeDepartment = employeeDepartmentComboBox.getValue();

        if (!employeeName.isEmpty()) {
            employees = employees.stream()
                    .filter(employee -> employee.getFirstName().toLowerCase().contains(employeeName.toLowerCase()) ||
                            employee.getLastName().toLowerCase().contains(employeeName.toLowerCase()))
                    .toList();
        }

        if (!employeeSalaryFromString.isEmpty()) {
            employees = employees.stream()
                    .filter(employee -> employee.getSalary().compareTo(new BigDecimal(employeeSalaryFromString)) >= 0)
                    .toList();
        }

        if (!employeeSalaryToString.isEmpty()) {
            employees = employees.stream()
                    .filter(employee -> employee.getSalary().compareTo(new BigDecimal(employeeSalaryToString)) <= 0)
                    .toList();
        }

        if (employeeDepartment != null) {
            employees = employees.stream()
                    .filter(employee -> employee.getDepartment() != null && employee.getDepartment().getName().equalsIgnoreCase(employeeDepartment))
                    .toList();
        }

        ObservableList<Employee> employeeObservableList = FXCollections.observableList(employees);
        employeeTableView.setItems(employeeObservableList);
    }

    public void deleteEmployee() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje zaposlenika");
        alert.setContentText("Jeste li sigurni da želite obrisati zaposlenika " + employee.getFirstName() + " " + employee.getLastName() + "?");
        alert.showAndWait();

        if (alert.getResult() != null && alert.getResult().getText().equals("OK")) {
            employeeRepository.delete(employee);
            filterEmployees();
        } else {
            alert.close();
        }
    }

    public void editEmployee() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();

        if (employee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/eperformance/employeeEditScreen.fxml"));
                SceneManager.switchScene(loader, "Uređivanje zaposlenika", 900, 500);
                EmployeeEditController employeeEditController = loader.getController();
                employeeEditController.setEmployee(employee);
            } catch (IOException e) {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }

    public void populateComboBox() {
        List<Department> departments = new DepartmentRepository().findAll();
        for (Department department : departments) {
            if (department.getName() != null) {
                employeeDepartmentComboBox.getItems().add(department.getName());
            }
        }
    }
}
