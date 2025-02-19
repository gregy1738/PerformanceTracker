package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.repository.DepartmentRepository;
import hr.javafx.eperformance.repository.EmployeeRepository;
import hr.javafx.eperformance.service.AuthService;
import hr.javafx.eperformance.service.IAuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class EmployeeNewController {

    @FXML
    private TextField employeeFirstNameTextField;

    @FXML
    private TextField employeeLastNameTextField;

    @FXML
    private TextField employeePasswordField;

    @FXML
    private TextField employeeJobTitleTextField;

    @FXML
    private TextField employeeSalaryTextField;

    @FXML
    private ComboBox<String> employeeDepartmentComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final DepartmentRepository departmentRepository = new DepartmentRepository();
    IAuthService authService = new AuthService();

    public void initialize() {
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            employeeDepartmentComboBox.getItems().add(department.getName());
        }
    }

    public void saveEmployee() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String firstName = employeeFirstNameTextField.getText();
        if (firstName.isEmpty()) {
            errorMessage.append("Ime je obavezno polje.\n");
        } else if (!firstName.matches("^[a-zA-Z]+$")) {
            errorMessage.append("Ime može sadržavati samo slova bez dijakritičkih znakova.\n");
        }
        String lastName = employeeLastNameTextField.getText();
        if (lastName.isEmpty()) {
            errorMessage.append("Prezime je obavezno polje.\n");
        } else if (!lastName.matches("^[a-zA-Z]+$")) {
            errorMessage.append("Prezime može sadržavati samo slova bez dijakritičkih znakova.\n");
        }

        String password = employeePasswordField.getText();
        if (password.isEmpty()) {
            errorMessage.append("Lozinka je obavezno polje.\n");
        }

        String jobTitle = employeeJobTitleTextField.getText();
        if (jobTitle.isEmpty()) {
            errorMessage.append("Naziv posla je obavezno polje.\n");
        }

        String salaryString = employeeSalaryTextField.getText();
        if (salaryString.isEmpty()) {
            errorMessage.append("Plaća je obavezno polje.\n");
        } else if (!salaryString.matches(("^([1-9]\\d{2,11}|0)(\\.\\d{1,2})?$"))) {
            errorMessage.append("Plaća mora biti pozitivan broj s najviše dvije decimale, veći od 99 i unesen u formatu, npr. 1000.00!\n");
        }

        String departmentName = employeeDepartmentComboBox.getValue();
        if (departmentName == null) {
            errorMessage.append("Odabir odjela je obavezno polje.\n");
        }

        Optional<Department> department = departmentRepository.findAll().stream()
                .filter(d -> d.getName().equalsIgnoreCase(departmentName))
                .findFirst();

        if (!errorMessage.isEmpty()) {
            errorAlert(errorMessage);

        } else {
            BigDecimal salary = new BigDecimal(salaryString);
            Employee employee = new Employee.Builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .jobTitle(jobTitle)
                    .salary(salary)
                    .department(department.orElse(null))
                    .build();
            employeeRepository.save(employee);
            authService.register(firstName, lastName, password);
            showSuccessDialog(firstName, lastName);
            SceneManager.switchScene("/hr/javafx/eperformance/employeeSearchScreen.fxml", "Pretraga zaposlenika", 900, 500);

        }
    }

    private static void errorAlert(StringBuilder errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogreška");
        alert.setHeaderText("Neispravni podaci");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
    }

    private void showSuccessDialog(String firstName, String lastName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje zaposlenika");
        alert.setHeaderText("Zaposlenik spremljen");
        alert.setContentText("Zaposlenik " + firstName + " " + lastName + " je uspješno spremljen.");
        alert.showAndWait();
    }

}
