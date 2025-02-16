package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.repository.DepartmentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        String name = departmentNameTextField.getText();
        String description = departmentDescriptionTextArea.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja odjela");
        alert.setContentText("Jeste li sigurni da želite urediti odjel " + name + "?");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            Department department = new Department(name, description);
            departmentRepository.update(department);
            SceneManager.switchScene("/hr/javafx/eperformance/departmentSearchScreen.fxml", "Pretraga odjela", 900, 500);
        } else {
            alert.close();
        }
    }
}
