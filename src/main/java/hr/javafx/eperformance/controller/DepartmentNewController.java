package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.exception.DepartmentAlreadyExistsException;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.repository.DepartmentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DepartmentNewController {

    @FXML
    private TextField departmentNameTextField;

    @FXML
    private TextArea departmentDescriptionTextArea;

    DepartmentRepository departmentRepository = new DepartmentRepository();

    public void saveDepartment() {
        String name = departmentNameTextField.getText();
        String description = departmentDescriptionTextArea.getText();

        try {
            Department department = new Department(name, description);
            departmentRepository.save(department);
            showSuccessDialog(name);
        } catch (DepartmentAlreadyExistsException e) {
            showErrorDialog(name);
        }

        departmentNameTextField.clear();
        departmentDescriptionTextArea.clear();
    }

    private static void showErrorDialog(String name) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Odjel već postoji");
        alert.setContentText("Odjel s imenom " + name + " već postoji.");
        alert.showAndWait();
    }

    private static void showSuccessDialog(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spremanje odjela");
        alert.setHeaderText("Odjel spremljen");
        alert.setContentText("Odjel " + name + " je uspješno spremljen.");
        alert.showAndWait();
    }

}
