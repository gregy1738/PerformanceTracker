package hr.javafx.eperformance;

import hr.javafx.eperformance.helper.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PerformanceTrackerApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setMainStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(PerformanceTrackerApplication.class.getResource("userLoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Prijava korisnika");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}