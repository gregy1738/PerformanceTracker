package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.helper.SceneManager;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.ChangeLog;
import hr.javafx.eperformance.model.PerformanceMetrics;
import hr.javafx.eperformance.model.PerformanceReview;
import hr.javafx.eperformance.repository.ChangeLogRepository;
import hr.javafx.eperformance.repository.PerformanceReviewRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class PerformanceReviewEditController {

    @FXML
    private ComboBox<String> performanceRatingComboBox;

    @FXML
    private TextField performanceReviewTextField;

    @FXML
    private TextArea performanceCommentTextArea;

    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();
    private Long id;

    public void initialize() {
        performanceRatingComboBox.getItems().addAll("1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5");
    }

    public void setPerformanceReview(PerformanceReview performanceReview) {
        id = performanceReview.getId();
        performanceRatingComboBox.setValue(String.valueOf(performanceReview.getPerformanceMetrics().getPerformanceRating()));
        performanceReviewTextField.setText(performanceReview.getReview());
        performanceCommentTextArea.setText(performanceReview.getComment());
    }

    public void confirmEdit() throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        String performanceRatingString = performanceRatingComboBox.getValue();
        if (performanceRatingString == null) {
            errorMessage.append("Unesite ocjenu performansi!\n");
        }

        String review = performanceReviewTextField.getText();
        if (review.isEmpty()) {
            errorMessage.append("Unesite opis izvještaja!\n");
        }

        String comment = performanceCommentTextArea.getText();

        Alert alert = confirmationAlert();

        if (!errorMessage.isEmpty()) {
            alertError(errorMessage);
        } else if (alert.getResult().getText().equals("OK")) {
            Optional<PerformanceReview> oldReviewOptional = performanceReviewRepository.findById(id);
            EmployeeType changedByRole = SessionManager.getLoggedInUser().getRole();
            String changedByEmail = SessionManager.getLoggedInUser().getEmail();

            double performanceRating = Double.parseDouble(performanceRatingString);
            PerformanceReview newReview = new PerformanceReview.Builder()
                    .id(id)
                    .performanceMetrics(new PerformanceMetrics(performanceRating))
                    .review(review)
                    .comment(comment)
                    .build();

            if (oldReviewOptional.isPresent()) {
                PerformanceReview oldReview = oldReviewOptional.get();
                changedValuesCheck(oldReview, newReview, changedByRole, changedByEmail);
            }

            performanceReviewRepository.update(newReview);
            SceneManager.switchScene("/hr/javafx/eperformance/performanceReviewSearchScreen.fxml", "Pretraga izvještaja", 900, 500);
        } else {
            alert.close();
        }
    }

    private static void alertError(StringBuilder errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Greška");
        errorAlert.setHeaderText("Neispravan unos");
        errorAlert.setContentText(errorMessage.toString());
        errorAlert.showAndWait();
    }

    private static Alert confirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda uređivanja");
        alert.setHeaderText("Potvrda uređivanja izvještaja");
        alert.setContentText("Jeste li sigurni da želite urediti izvještaj?");
        alert.showAndWait();
        return alert;
    }

    private static void changedValuesCheck(PerformanceReview oldReview, PerformanceReview newReview, EmployeeType changedByRole, String changedByEmail) {
        if (oldReview.getPerformanceMetrics().getPerformanceRating() != newReview.getPerformanceMetrics().getPerformanceRating()) {
            ChangeLogRepository.saveChange(new ChangeLog("Ocjena performansi",
                    String.valueOf(oldReview.getPerformanceMetrics().getPerformanceRating()),
                    String.valueOf(newReview.getPerformanceMetrics().getPerformanceRating()),
                    changedByEmail, changedByRole));
        }
        if (!oldReview.getReview().equals(newReview.getReview())) {
            ChangeLogRepository.saveChange(new ChangeLog("Opis izvještaja",
                    oldReview.getReview(), newReview.getReview(), changedByEmail, changedByRole));
        }
        if (!oldReview.getComment().equals(newReview.getComment())) {
            ChangeLogRepository.saveChange(new ChangeLog("Komentar",
                    oldReview.getComment(), newReview.getComment(), changedByEmail, changedByRole));
        }
    }

}
