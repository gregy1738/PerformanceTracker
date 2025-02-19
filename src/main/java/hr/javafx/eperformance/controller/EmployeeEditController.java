package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.ChangeLog;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.repository.ChangeLogRepository;
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
        if(employee.getDepartment() != null){
            employeeDepartmentComboBox.setValue(employee.getDepartment().getName());
        }
    }

    public void confirmEdit() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String firstName = employeeFirstNameTextField.getText();
        if(firstName.isEmpty()){
            errorMessage.append("Ime je obavezno polje!\n");
        }else if(!firstName.matches("^[a-zA-Z]+$")){
            errorMessage.append("Ime može sadržavati samo slova bez dijakritičkih znakova.\n");
        }

        String lastName = employeeLastNameTextField.getText();
        if(lastName.isEmpty()){
            errorMessage.append("Prezime je obavezno polje!\n");
        }else if (!lastName.matches("^[a-zA-Z]+$")) {
            errorMessage.append("Prezime može sadržavati samo slova bez dijakritičkih znakova.\n");
        }

        String jobTitle = employeeJobTitleTextField.getText();
        if(jobTitle.isEmpty()){
            errorMessage.append("Naziv radnog mjesta je obavezno polje!\n");
        }

        String salaryString = employeeSalaryTextField.getText();
        if(salaryString.isEmpty()){
            errorMessage.append("Plaća je obavezno polje!\n");
        } else if (!salaryString.matches(("^([1-9]\\d{2,11}|0)(\\.\\d{1,2})?$"))) {
            errorMessage.append("Plaća mora biti pozitivan broj s najviše dvije decimale, veći od 99 i unesen u formatu, npr. 1000.00!\n");
        }

        String departmentName = employeeDepartmentComboBox.getValue();
        if(departmentName == null){
            errorMessage.append("Odabir odjela je obavezno polje!\n");
        }

        Optional<Department> department = new DepartmentRepository().findByName(departmentName);

        Alert alert = confirmationAlert(firstName, lastName);

        if(!errorMessage.isEmpty()){

            errorAlert(errorMessage);

        }else if (alert.getResult().getText().equals("OK")) {

            BigDecimal salary = new BigDecimal(salaryString);
            Employee newEmployee = new Employee.Builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .jobTitle(jobTitle)
                    .salary(salary)
                    .department(department.orElse(null))
                    .build();

            Optional<Employee> oldEmployeeOptional = employeeRepository.findById(id);
            EmployeeType changedByRole = SessionManager.getLoggedInUser().getRole();
            String changedByEmail = SessionManager.getLoggedInUser().getEmail();

            if(oldEmployeeOptional.isPresent()) {
                Employee oldEmployee = oldEmployeeOptional.get();
                changedValuesCheck(oldEmployee, newEmployee, changedByRole, changedByEmail);
            }

            employeeRepository.update(newEmployee);
            SceneManager.switchScene("/hr/javafx/eperformance/employeeSearchScreen.fxml", "Pretraga zaposlenika", 900, 500);

        } else {
            alert.close();
        }
    }

    private static void errorAlert(StringBuilder errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Pogreška");
        errorAlert.setHeaderText("Neispravni podaci");
        errorAlert.setContentText(errorMessage.toString());
        errorAlert.showAndWait();
    }

    private static Alert confirmationAlert(String firstName, String lastName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja zaposlenika");
        alert.setContentText("Jeste li sigurni da želite urediti zaposlenika " + firstName + " " + lastName + "?");
        alert.showAndWait();
        return alert;
    }

    private static void changedValuesCheck(Employee oldEmployee, Employee newEmployee, EmployeeType changedByRole, String changedByEmail) {
        if (!oldEmployee.getFirstName().equals(newEmployee.getFirstName())) {
            ChangeLogRepository.saveChange(new ChangeLog("Ime zaposlenika",
                    oldEmployee.getFirstName(), newEmployee.getFirstName(), changedByEmail, changedByRole));
        }
        if (!oldEmployee.getLastName().equals(newEmployee.getLastName())) {
            ChangeLogRepository.saveChange(new ChangeLog("Prezime zaposlenika",
                    oldEmployee.getLastName(), newEmployee.getLastName(), changedByEmail, changedByRole));
        }
        if (!oldEmployee.getJobTitle().equals(newEmployee.getJobTitle())) {
            ChangeLogRepository.saveChange(new ChangeLog("Naziv radnog mjesta",
                    oldEmployee.getJobTitle(), newEmployee.getJobTitle(), changedByEmail, changedByRole));
        }
        if (oldEmployee.getSalary().compareTo(newEmployee.getSalary()) != 0) {
            ChangeLogRepository.saveChange(new ChangeLog("Plaća",
                    oldEmployee.getSalary().toString(), newEmployee.getSalary().toString(), changedByEmail, changedByRole));
        }
        if(oldEmployee.getDepartment() == null && newEmployee.getDepartment() != null) {
            ChangeLogRepository.saveChange(new ChangeLog("Odjel",
                    "Nepostojeći odjel", newEmployee.getDepartment().getName(), changedByEmail, changedByRole));
        }
        else if (!oldEmployee.getDepartment().getName().equals(newEmployee.getDepartment().getName())) {
            ChangeLogRepository.saveChange(new ChangeLog("Odjel",
                    oldEmployee.getDepartment().getName(), newEmployee.getDepartment().getName(), changedByEmail, changedByRole));
        }
    }
}
