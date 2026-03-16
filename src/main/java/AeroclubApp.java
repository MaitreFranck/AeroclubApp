import database.DatabaseManager;
import database.UserRepository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AeroclubApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.initDatabase();
        UserRepository repo = new UserRepository();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Aéroclub - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}