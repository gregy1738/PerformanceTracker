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
import java.util.Optional;

public class EmployeeEditController {

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
    private Long id;

    public void initialize(){
        List<Department> departments = new DepartmentRepository().findAll();
        for (Department department : departments) {
            employeeDepartmentComboBox.getItems().add(department.getName());
        }
    }

    public void setEmployee(Employee employee){
        id = employee.getId();
        employeeFirstNameTextField.setText(employee.getFirstName());
        employeeLastNameTextField.setText(employee.getLastName());
        employeeJobTitleTextField.setText(employee.getJobTitle());
        employeeSalaryTextField.setText(String.valueOf(employee.getSalary()));
        employeeDepartmentComboBox.setValue(employee.getDepartment().getName());
    }

    public void confirmEdit() throws IOException {
        String firstName = employeeFirstNameTextField.getText();
        String lastName = employeeLastNameTextField.getText();
        String jobTitle = employeeJobTitleTextField.getText();
        String salaryString = employeeSalaryTextField.getText();
        BigDecimal salary = new BigDecimal(salaryString);
        String departmentName = employeeDepartmentComboBox.getValue();

        Optional<Department> department = new DepartmentRepository().findByName(departmentName);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja zaposlenika");
        alert.setContentText("Jeste li sigurni da želite urediti zaposlenika " + firstName + " " + lastName + "?");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            Employee employee = new Employee.Builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .jobTitle(jobTitle)
                    .salary(salary)
                    .department(department.orElse(null))
                    .build();
            employeeRepository.update(employee);
            SceneManager.switchScene("/hr/javafx/eperformance/employeeSearchScreen.fxml", "Pretraga zaposlenika", 900, 500);
        } else {
            alert.close();
        }
    }




}
