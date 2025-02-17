package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.DepartmentRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentSearchController {

    @FXML
    private TextField departmentNameTextField;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, String> departmentNameColumn;

    @FXML
    private TableColumn<Department, String> departmentDescriptionColumn;

    @FXML
    private TableColumn<Department, String> departmentEmployeesColumn;

    @FXML
    private Button editDepartmentButton;

    @FXML
    private Button deleteDepartmentButton;

    private final DepartmentRepository departmentRepository = new DepartmentRepository();

    public void initialize() {
        User loggedInUser = SessionManager.getLoggedInUser();

        if(loggedInUser == null || !loggedInUser.getRole().equals(EmployeeType.DIRECTOR)) {
            editDepartmentButton.setVisible(false);
            deleteDepartmentButton.setVisible(false);
        }

        departmentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        departmentDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        departmentEmployeesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployees().stream()
                .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                .collect(Collectors.joining(", "))));
    }

    public void filterDepartments() {
        List<Department> departments = departmentRepository.findAll();

        String departmentName = departmentNameTextField.getText();

        if (!departmentName.isEmpty()) {
            departments = departments.stream()
                    .filter(department -> department.getName().toLowerCase().contains(departmentName.toLowerCase()))
                    .toList();
        }

        ObservableList<Department> observableDepartments = FXCollections.observableList(departments);

        departmentTableView.setItems(observableDepartments);

        departmentNameTextField.clear();

    }

    public void deleteDepartment() {
        Department department = departmentTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje odjela");
        alert.setContentText("Jeste li sigurni da želite obrisati odjel " + department.getName() + "?");
        alert.showAndWait();

        if (alert.getResult() != null && alert.getResult().getText().equals("OK")) {
            departmentRepository.delete(department);
            filterDepartments();
        } else {
            alert.close();
        }
    }

    public void editDepartment() {
        Department department = departmentTableView.getSelectionModel().getSelectedItem();

        if (department != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/eperformance/departmentEditScreen.fxml"));
                SceneManager.switchScene(loader, "Uređivanje odjela", 900, 500);

                DepartmentEditController departmentEditController = loader.getController();
                departmentEditController.setDepartment(department);
            } catch (IOException e) {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }
}
