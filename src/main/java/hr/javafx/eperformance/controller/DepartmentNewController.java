package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.exception.DepartmentAlreadyExistsException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.repository.DepartmentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DepartmentNewController {

    @FXML
    private TextField departmentNameTextField;

    @FXML
    private TextArea departmentDescriptionTextArea;

    DepartmentRepository departmentRepository = new DepartmentRepository();

    public void saveDepartment() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String name = departmentNameTextField.getText();
        if (name.isEmpty()) {
            errorMessage.append("Ime odjela je obavezno.\n");
        }else if(!name.matches("^[a-zA-Z0-9 ]+$")){
            errorMessage.append("Ime odjela može sadržavati samo slova i brojeve.\n");
            LoggerUtil.logInfo("Ime odjela može sadržavati samo slova i brojeve.");
        }

        String description = departmentDescriptionTextArea.getText();

        if(!errorMessage.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Neispravni podaci");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }else {
            try {
                Department department = new Department(name, description);
                departmentRepository.save(department);
                showSuccessDialog(name);
                SceneManager.switchScene("/hr/javafx/eperformance/departmentSearchScreen.fxml", "Pretraga odjela", 900, 500);
            } catch (DepartmentAlreadyExistsException e) {
                showErrorDialog(name);
            }
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
