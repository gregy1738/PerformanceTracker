package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.ChangeLog;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.repository.ChangeLogRepository;
import hr.javafx.eperformance.repository.DepartmentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class DepartmentEditController {

    @FXML
    private TextField departmentNameTextField;

    @FXML
    private TextArea departmentDescriptionTextArea;

    private final DepartmentRepository departmentRepository = new DepartmentRepository();

    public void setDepartment(Department department) {
        departmentNameTextField.setText(department.getName());
        departmentDescriptionTextArea.setText(department.getDescription());
    }

    public void confirmEdit() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String name = departmentNameTextField.getText();
        if(name.isEmpty()){
            errorMessage.append("Naziv je obavezno polje.\n");
        }

        String description = departmentDescriptionTextArea.getText();
        if(description.isEmpty()){
            errorMessage.append("Opis je obavezno polje.\n");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja odjela");
        alert.setContentText("Jeste li sigurni da želite urediti odjel " + name + "?");
        alert.showAndWait();

        if(!errorMessage.isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Neispravni podaci");
            errorAlert.setContentText(errorMessage.toString());
            errorAlert.showAndWait();
        }else if (alert.getResult().getText().equals("OK")) {
            Optional<Department> oldDepartmentOptional = departmentRepository.findByName(name);
            EmployeeType changedByRole = SessionManager.getLoggedInUser().getRole();
            String changedByEmail = SessionManager.getLoggedInUser().getEmail();

            Department newDepartment = new Department(name, description);

            if (oldDepartmentOptional.isPresent()) {
                Department oldDepartment = oldDepartmentOptional.get();
                changedValuesCheck(oldDepartment, newDepartment, changedByRole, changedByEmail);
            }

            departmentRepository.update(newDepartment);
            SceneManager.switchScene("/hr/javafx/eperformance/departmentSearchScreen.fxml", "Pretraga odjela", 900, 500);
        } else {
            alert.close();
        }
    }

    private static void changedValuesCheck(Department oldDepartment, Department newDepartment, EmployeeType changedByRole, String changedByEmail) {
        if (!oldDepartment.getName().equals(newDepartment.getName())) {
            ChangeLogRepository.saveChange(new ChangeLog("Naziv odjela",
                    oldDepartment.getName(), newDepartment.getName(), changedByEmail, changedByRole));
        }
        if (!oldDepartment.getDescription().equals(newDepartment.getDescription())) {
            ChangeLogRepository.saveChange(new ChangeLog("Opis odjela",
                    oldDepartment.getDescription(), newDepartment.getDescription(), changedByEmail, changedByRole));
        }
    }

}
