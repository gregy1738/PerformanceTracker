package hr.javafx.eperformance.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Helper class for switching scenes in JavaFX application.
 */

public class SceneManager {

    private static Stage mainStage;

    private SceneManager() {
    }

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    /**
     * Switches the scene to the one specified by the fxmlPath.
     *
     * @param fxmlPath path to the fxml file
     * @param title    title of the scene
     * @param width    width of the scene
     * @param height   height of the scene
     * @throws IOException if the fxml file is not found
     */

    public static void switchScene(String fxmlPath, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        switchScene(loader, title, width, height);
    }

    public static void switchScene(FXMLLoader loader, String title, int width, int height) throws IOException {
        Scene scene = new Scene(loader.load(), width, height);
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        mainStage.show();
    }
}
