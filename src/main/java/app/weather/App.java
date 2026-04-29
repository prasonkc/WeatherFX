package app.weather;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the JavaFX weather application.
 *
 * @author Prason KC
 */
public class App extends Application {

    /**
     * Creates and displays the main application window.
     *
     * <p>Precondition: the weather FXML layout must be available at
     * {@code /view/weather_view.fxml}.
     *
     * @param stage the primary stage supplied by the JavaFX runtime
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/weather_view.fxml")
        );
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("What's the weather today?");
        stage.show();
    }

    /**
     * Starts the JavaFX application lifecycle.
     *
     * <p>Precondition: this method must be called from a standard Java entry
     * point.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch();
    }
}
