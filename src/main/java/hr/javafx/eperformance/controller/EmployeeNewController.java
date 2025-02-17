package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.repository.DepartmentRepository;
import hr.javafx.eperformance.repository.EmployeeRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class EmployeeNewController {

    @FXML
    private TextField employeeFirstNameTextField;

    @FXML
    private TextField employeeLastNameTextField;

    @FXML
    private TextField employeeJobTitleTextField;

    @FXML
    private TextField employeeSalaryTextField;

    @FXML
    private ComboBox<String> employeeDepartmentComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final DepartmentRepository departmentRepository = new DepartmentRepository();

    public void initialize() {
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            employeeDepartmentComboBox.getItems().add(department.getName());
        }
    }

    public void saveEmployee() throws IOException {
        String firstName = employeeFirstNameTextField.getText();
        String lastName = employeeLastNameTextField.getText();
        String jobTitle = employeeJobTitleTextField.getText();
        String salaryString = employeeSalaryTextField.getText();
        BigDecimal salary = new BigDecimal(salaryString);
        String departmentName = employeeDepartmentComboBox.getValue();

        Department department = departmentRepository.findAll().stream()
                .filter(d -> d.getName().equalsIgnoreCase(departmentName))
                .findFirst()
                .orElse(null);

        Employee employee = new Employee.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .jobTitle(jobTitle)
                .salary(salary)
                .department(department)
                .build();
        employeeRepository.save(employee);
        showSuccessDialog(firstName, lastName);
        SceneManager.switchScene("/hr/javafx/eperformance/employeeSearchScreen.fxml", "Pretraga zaposlenika", 900, 500);
    }

    private void showSuccessDialog(String firstName, String lastName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje zaposlenika");
        alert.setHeaderText("Zaposlenik spremljen");
        alert.setContentText("Zaposlenik " + firstName + " " + lastName + " je uspješno spremljen.");
        alert.showAndWait();
    }

}
