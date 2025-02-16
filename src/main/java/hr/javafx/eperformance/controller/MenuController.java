package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.helper.SceneManager;

import java.io.IOException;

public class MenuController {

    public void showDepartmentSearchScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/departmentSearchScreen.fxml", "Pretraga odjela", 900, 500);
    }

    public void showDepartmentNewScreen() throws IOException {
        SceneManager.switchScene("/hr/javafx/eperformance/departmentNewScreen.fxml", "Dodaj odjel", 900, 500);
    }
}
