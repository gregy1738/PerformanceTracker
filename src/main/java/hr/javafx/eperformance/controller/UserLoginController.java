package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.exception.InvalidCredentialsException;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.service.AuthService;
import hr.javafx.eperformance.service.IAuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private TextField userEmailTextField;

    @FXML
    private TextField userPasswordField;

    IAuthService authService = new AuthService();

    public void confirmLogin() throws IOException {
        String email = userEmailTextField.getText();
        String password = userPasswordField.getText();

        try {
            authService.login(email, password);
            SceneManager.switchScene("/hr/javafx/eperformance/welcomeScreen.fxml", "Dobrodošli", 700, 500);
        } catch (InvalidCredentialsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Neuspješna prijava");
            alert.setContentText("Molim unesite ispravne podatke za prijavu.");
            alert.showAndWait();
            userPasswordField.clear();
        }
    }
}
